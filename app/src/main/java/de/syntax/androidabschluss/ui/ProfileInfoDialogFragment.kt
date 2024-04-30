import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import de.syntax.androidabschluss.databinding.FragmentLoginBinding
import de.syntax.androidabschluss.databinding.FragmentProfileInfoDialogBinding
import de.syntax.androidabschluss.ui.authentication.FirestoreViewModel

class ProfileInfoDialogFragment : DialogFragment() {
    private val viewModel: FirestoreViewModel by viewModels()

    private lateinit var userMail: String


    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val dialogBinding = FragmentProfileInfoDialogBinding.inflate(inflater)
        val dialog = Dialog(requireContext())

        dialog.setContentView(dialogBinding.root)

        // Anpassen der Breite des Dialogs
        val width =
            resources.displayMetrics.widthPixels * 0.8 // Zum Beispiel 80% der Bildschirmbreite
        dialog.window?.setLayout(
            width.toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        ) // Höhe auf WRAP_CONTENT setzen

        // Setzen des abgerundeten Radius für den Dialoghintergrund
        val drawable = GradientDrawable()
        drawable.cornerRadius = resources.displayMetrics.density * 20 // Radius in dp umwandeln
        dialog.window?.setBackgroundDrawable(drawable)

        // Beobachte das currentUser-LiveData-Objekt im ViewModel, um Benutzerdaten anzuzeigen
        viewModel.currentUser.observe(this) { user ->
            user?.let {
                userMail = it.email.toString()
                dialogBinding.tvNutzerName.text = userMail
            }
        }


        dialogBinding.btnLogout.setOnClickListener {
            viewModel.logout()
            requireActivity().finishAffinity() // Aktivität beenden
        }


        dialogBinding.linkedin.setOnClickListener {
            openLinkedInProfile()

        }

        dialogBinding.github.setOnClickListener {
            openGithubProfile()
        }

        return dialog

    }

    fun openLinkedInProfile() {
        val linkedInProfileUrl = "https://www.linkedin.com/in/michel-lobbos-a2a741303"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInProfileUrl))
        startActivity(intent)
    }

    fun openGithubProfile() {
        val linkedInProfileUrl = "https://github.com/MichelLobbos-Syntax"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInProfileUrl))
        startActivity(intent)
    }
}
