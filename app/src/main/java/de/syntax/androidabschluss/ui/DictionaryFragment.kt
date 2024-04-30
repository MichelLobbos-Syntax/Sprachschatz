package de.syntax.androidabschluss.ui

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.models.WordResult
import de.syntax.androidabschluss.data.remote.RetrofitInstance
import de.syntax.androidabschluss.databinding.FragmentDictionaryBinding
import de.syntax.androidabschluss.util.MeaningAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale


class DictionaryFragment : Fragment() {
    //SPEECH
    private val RQ_SPEECH_REC = 102
    private lateinit var tts: TextToSpeech


    lateinit var adapter: MeaningAdapter
    private lateinit var binding: FragmentDictionaryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDictionaryBinding.inflate(layoutInflater)
        binding.mike.setOnClickListener {
            askSpeechInput()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.tv_titel).text = "DICTIONARY"



        binding.editTextSearch.setOnEditorActionListener { textView, _, _ ->
            hideKeyboard()
            val searchQuery = textView.text.toString()
            // Bedeutung des eingegebenen Wortes abrufen
            val world = binding.editTextSearch.text.toString()

            getMeaning(world)


            true


        }


        // Adapter für die RecyclerView einrichten
        adapter = MeaningAdapter(emptyList())
        binding.rvMeaning.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMeaning.adapter = adapter


    }


    /*
        private fun speak(text: String) {
            // Überprüfen, ob die TextToSpeech-Engine initialisiert ist
            if (::tts.isInitialized) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Log.e("ArtikelFragment", "TextToSpeech is not initialized.")
            }
        }

    */

    private fun hideKeyboard() {
        val hideKeyboard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        hideKeyboard.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getMeaning(word: String) {
        // Bedeutung des übergebenen Wortes abrufen
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.retrofitService.getmeaning(word)
                if (response.isSuccessful) {
                    response.body()?.first()?.let {

                        setUI(it)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }

    }


    // UI mit den Daten des Wortergebnisses aktualisieren
    private fun setUI(response: WordResult) {
        requireActivity().runOnUiThread {
            binding.tvWord.text = response.word
            binding.tvPhonetic.text = response.phonetic
            adapter.updateNewData(response.meanings)
        }
    }
















    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Ergebnis des Spracherkennungsintents verarbeiten
        if (requestCode == RQ_SPEECH_REC && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!result.isNullOrEmpty()) {
                binding.editTextSearch.setText(result[0])
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


    // Methode zum Starten der Spracheingabeaufforderung
    private fun askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Speech recognition is not available",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
            startActivityForResult(i, RQ_SPEECH_REC)

        }
    }


}