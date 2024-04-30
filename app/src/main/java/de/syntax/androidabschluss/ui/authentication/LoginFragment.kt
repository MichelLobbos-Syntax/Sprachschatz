package de.syntax.androidabschluss.ui.authentication
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val viewModel: FirestoreViewModel by activityViewModels()

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Zurück-Navigations-Button konfigurieren
        binding.bnBack.setOnClickListener {
            findNavController().navigateUp()
        }


        // Anmelde-Button konfigurieren
        binding.bnAnmelden.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()



            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                viewModel.login(email, password)



            }else{
                Toast.makeText(context, "bitte ausfühllen", Toast.LENGTH_SHORT).show()
            }
        }



    }
}