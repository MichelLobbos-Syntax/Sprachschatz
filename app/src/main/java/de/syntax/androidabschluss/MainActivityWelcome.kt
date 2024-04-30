package de.syntax.androidabschluss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import de.syntax.androidabschluss.databinding.ActivityMainWelcomeBinding
import de.syntax.androidabschluss.ui.authentication.FirestoreViewModel

class MainActivityWelcome : AppCompatActivity() {
    private val viewModel: FirestoreViewModel by viewModels()

    private lateinit var binding: ActivityMainWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.currentUser.observe(this) { user ->
            if (user != null) {
                // Navigiere zur Hauptaktivität (mainActivity3)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Beende die aktuelle Aktivität
            }
        }
    }
}