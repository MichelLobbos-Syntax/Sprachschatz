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
import de.syntax.androidabschluss.databinding.FragmentRegistierenBinding

class RegistierenFragment : Fragment() {
    private val viewModel: FirestoreViewModel by activityViewModels()

    private lateinit var binding: FragmentRegistierenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistierenBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bnRegistrieren.setOnClickListener {


            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val repPassword = binding.etRepPassword.text.toString()

            if (!email.isNullOrEmpty() && !password.isNullOrEmpty() && password == repPassword) {
                viewModel.signup(email, password)
            }

            if (password != repPassword){
                Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
            }

        }


        binding.bnBack.setOnClickListener {
            findNavController().navigateUp()

        }
        // Observer zum Überwachen des aktuellen Benutzers
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            // Navigiere zur Hauptaktivität, wenn ein Benutzer angemeldet ist
            if (user != null) {
                findNavController().navigate(R.id.mainActivity3)
            }
        }
    }




}