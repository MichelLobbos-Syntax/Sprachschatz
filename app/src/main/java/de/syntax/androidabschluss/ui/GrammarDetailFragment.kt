package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentGrammarBinding
import de.syntax.androidabschluss.databinding.FragmentGrammarDetailBinding
import de.syntax.androidabschluss.ui.authentication.FirestoreViewModel


class GrammarDetailFragment : Fragment() {
    private lateinit var binding: FragmentGrammarDetailBinding
    private val viewModel: FirestoreViewModel by activityViewModels()

    private var id = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Argumente aus dem Bundle extrahieren, falls vorhanden
        arguments?.let {

            id = it.getString("id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGrammarDetailBinding.inflate(layoutInflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Klicklistener für die Zurück-Schaltfläche einrichten, um zum vorherigen Fragment zu navigieren
        binding.bnBack.setOnClickListener {
            // Grammatikeintrag mit der passenden ID finden
            findNavController().navigate(R.id.grammarFragment)
        }

        // Daten im ViewModel beobachten und entsprechende UI-Aktualisierungen vornehmen
        viewModel.grammatik.observe(viewLifecycleOwner) { grammatikList ->
            val grammatik = grammatikList.find { it.id == id }
            // Wenn ein passender Eintrag gefunden wurde, Titel und Beschreibung setzen
            if (grammatik != null){
                binding.tvTitle.text = grammatik.title
                binding.tvDescription.text = grammatik.description
            }

        }
        Log.d("GrammatikDetailFragment", "ID: $id")
    }
}