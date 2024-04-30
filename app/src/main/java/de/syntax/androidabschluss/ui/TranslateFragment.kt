package de.syntax.androidabschluss.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.models.Saved
import java.util.Locale
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import de.syntax.androidabschluss.databinding.FragmentTranslateBinding


class TranslateFragment : Fragment() {


    private var isclickt: Boolean = false


    // camera
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val REQUESR_IMAGE_CAPURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE =
        100 // Hier kannst du die Nummer deiner Wahl verwenden


    // Spracherkennung-Anforderungscode
    private val RQ_SPEECH_REC = 102
    private lateinit var tts: TextToSpeech
    private lateinit var binding: FragmentTranslateBinding
    private val viewModel: SavedViewModel by activityViewModels()


    private var items = arrayOf("English", "German", "Arabic", "Spanish", "Russian")
    private var conditions =
        DownloadConditions.Builder().requireWifi().build() // Downloadbedingungen für Modelle


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTranslateBinding.inflate(layoutInflater)


        return binding.root
    }


    @SuppressLint("ServiceCast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.tv_titel).text = "TRANSLATE"

        // Adapter für die Sprachauswahl festlegen
        val itemAdapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            items
        )

        //SPEECH
        binding.ibMike.setOnClickListener {
            askSpeechInput()
        }

        binding.languageFrom.setAdapter(itemAdapter)        // Sprachauswahl "Von"
        binding.languageTo.setAdapter(itemAdapter)          // Sprachauswahl "Nach"





        binding.ivTranslate.setOnClickListener {
            isclickt = true
            hideKeyboard()

            // Optionen für den Übersetzer festlegen
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val translator = Translation.getClient(options)

            // Erforderliche Modelle herunterladen
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    // Model wurde erfolgreich heruntergeladen oder war bereits vorhanden
                    translator.translate(binding.edInpit.text.toString())
                        .addOnSuccessListener {
                            // Übersetzung erfolgreich
                            binding.output.text = it

                        }
                        .addOnFailureListener {
                            // Fehler bei der Übersetzung
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    // Fehler beim Herunterladen des Modells
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
        }


        binding.ibCopie.setOnClickListener {


            val textToCopy = binding.output.text.toString()

            // Zugriff auf den System-Clipboard
            val clipboardManager =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Erstellen einer ClipData mit dem zu kopierenden Text
            val clipData = ClipData.newPlainText("Text", textToCopy)

            // Setzen der ClipData ins Clipboard
            clipboardManager.setPrimaryClip(clipData)

            binding.ibCopie.setBackgroundResource(R.drawable.ic_copie_onclick)

            // Nach einer Sekunde den Hintergrund wieder auf ic_save zurücksetzen
            android.os.Handler().postDelayed({
                binding.ibCopie.setBackgroundResource(R.drawable.ic_copie)
            }, 1000)
        }




        binding.ibGespeichert.setOnClickListener {

            binding.ibGespeichert.setBackgroundResource(R.drawable.ic_save_onclick)

            // Nach einer Sekunde den Hintergrund wieder auf ic_save zurücksetzen
            android.os.Handler().postDelayed({
                binding.ibGespeichert.setBackgroundResource(R.drawable.ic_save)
            }, 1000)

            // Werte holen und speichern
            getValuesAndSave()

        }


        // Initialisierung der TextToSpeech
        tts = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {

                val selectedLanguage = selectTo()
                val locale = Locale(selectedLanguage)
                tts.language = Locale.getDefault()
            }
        }




        binding.ibSpeach.setOnClickListener {
            val textToSp = binding.output.text.toString()
            speak(textToSp)

        }


        binding.ivSwitch.setOnClickListener {
            // Create a new RotateAnimation for rotating the ImageView
            val rotateAnimation = RotateAnimation(
                0f, 180f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnimation.duration = 200 // Duration of the rotation animation in milliseconds
            rotateAnimation.repeatCount = 0 // Do not repeat the animation

            // Apply the animation to tauschen ImageView
            binding.ivSwitch.startAnimation(rotateAnimation)

            val x = binding.languageFrom.text.toString()
            binding.languageFrom.setText(binding.languageTo.text.toString())
            binding.languageTo.setText(x)
        }

        binding.ibScan.setOnClickListener {
            requestCameraPermission()
        }

        binding.ibEsc.setOnClickListener {
            binding.edInpit.setText("") // Leer die EditText-Ansicht
        }

        binding.edInpit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isclickt) {
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(selectFrom())
                        .setTargetLanguage(selectTo())
                        .build()

                    val translator = Translation.getClient(options)

                    translator.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            // Model wurde erfolgreich heruntergeladen oder war bereits vorhanden
                            translator.translate(binding.edInpit.text.toString())
                                .addOnSuccessListener {
                                    binding.output.text = it

                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener {
                            // Fehler beim Herunterladen des Modells
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }

                }
            }

            override fun afterTextChanged(s: Editable?) {

                binding.output.text = ""
            }
        })


    }


    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Wenn die Berechtigung bereits vorhanden ist, kannst du die Kamera öffnen
            takeImage()
        }
    }

    private fun takeImage() {
        // Starte die Kamera-App, um ein Bild aufzunehmen
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(intent, REQUESR_IMAGE_CAPURE)
        } catch (e: Exception) {
            Log.e("Camera Error", e.message ?: "Unknown error")
        }
    }

    private fun processImage(imageBitmap: Bitmap) {
        // Verarbeite das aufgenommene Bild, um den Text zu extrahieren
        val image = InputImage.fromBitmap(imageBitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Extrahiere den gescannten Text und setze ihn in das EditText-Feld
                val scannedText = visionText.text
                binding.edInpit.setText(scannedText)
            }
            .addOnFailureListener { e ->
                Log.e("Text Recognition", "Text recognition failed: ${e.message}")
                Toast.makeText(context, "Text recognition failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun hideKeyboard() {
        // Verberge die Bildschirmtastatur
        val hideKeyboard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


    // Funktion, um den Text vorzulesen
    private fun speak(text: String) {


        // Überprüfen, ob die TextToSpeech-Engine initialisiert ist
        if (::tts.isInitialized) {
            val selectedLanguage = selectTo()
            val locale = Locale(selectedLanguage)
            tts.language = locale

            // Ändern der Hintergrundfarbe des Buttons beim Klicken
            binding.ibSpeach.setBackgroundResource(R.drawable.ic_volume_onclick)

            // Text zur Sprachausgabe hinzufügen
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

            // Verzögerung einführen, um sicherzustellen, dass die Sprachausgabe gestartet wurde
            android.os.Handler().postDelayed({
                // Zurücksetzen der Hintergrundfarbe des Buttons nach einer Verzögerung von 100 Millisekunden
                binding.ibSpeach.setBackgroundResource(R.drawable.ic_volume)
            }, 1000)
        } else {
            Log.e("TranslateFragment", "TextToSpeech is not initialized.")
        }
    }

    override fun onDestroy() {
        // TextToSpeech-Engine stoppen und freigeben, wenn das Fragment zerstört wird
        tts.stop()
        tts.shutdown()
        super.onDestroy()
    }

// Funktion zum Speichern der eingegebenen und übersetzten Werte
    private fun getValuesAndSave() {
        val input = binding.edInpit.text.toString()
        val output = binding.output.text.toString()
        val new = Saved(input = input, output = output)
        viewModel.insertGespeichert(new)
        Toast.makeText(requireContext(), "Data saved successfully!", Toast.LENGTH_SHORT).show()

    }

    //translate From
    private fun selectFrom(): String {

        val selectedLanguage = binding.languageFrom.text.toString()
        // Rückgabe der ausgewählten Sprache
        return when (selectedLanguage) {
            "" -> "en"
            "English" -> "en"
            "German" -> "de"
            "Arabic" -> "ar"
            "Spanish" -> "es"
            "Russian" -> "ru"
            else -> "en" // Standard-Sprache festlegen
        }
    }
    //translate To
    private fun selectTo(): String {
        // Implementieren Sie Ihre Logik hier, um die Zielsprache auszuwählen
        val selectedLanguage = binding.languageTo.text.toString()
        return when (selectedLanguage) {
            "" -> "de"
            "English" -> "en"
            "German" -> "de"
            "Arabic" -> "ar"
            "Spanish" -> "es"
            "Russian" -> "ru"
            else -> "de" // H Standard-Sprache festlegen
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verarbeite das Ergebnis der Bildaufnahme
        if (requestCode == REQUESR_IMAGE_CAPURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                // Wenn ein Bild erfolgreich aufgenommen wurde, verarbeite es
                processImage(imageBitmap)
            } else {
                //wenn das Bild nicht aufgenommen werden konnte
                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }


        // Verarbeite das Ergebnis der Spracherkennung
        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                binding.edInpit.setText(result[0])
            } else {
                Log.e("SpeechRecognition", "Received null or empty result")
                Toast.makeText(
                    requireContext(),
                    "Error: Speech recognition result is null or empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun askSpeechInput() {
        val selectedLanguage = selectFrom()
        Log.d("SelectedLanguage", selectedLanguage) // Überprüfen der ausgewählten Sprache

        val locale = when (selectedLanguage) {
            "en" -> Locale.ENGLISH
            "de" -> Locale.GERMAN
            "ar" -> Locale("ar")
            "es" -> Locale("es")
            "ru" -> Locale("ru")
            else -> Locale.ENGLISH
        }

        Log.d("SelectedLocale", locale.toString()) // Überprüfen der festgelegten Locale

        // Erstelle eine Absicht zur Spracheingabe
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")

        // Starte die Aktivität zur Spracheingabe, wenn verfügbar
        if (speechIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivityForResult(speechIntent, RQ_SPEECH_REC)
        } else {
            Toast.makeText(
                requireContext(),
                "Speech recognition is not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
