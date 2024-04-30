package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.models.GrammatikData
import de.syntax.androidabschluss.databinding.FragmentGrammarBinding
import de.syntax.androidabschluss.ui.authentication.FirestoreViewModel
import de.syntax.androidabschluss.ui.authentication.TAG

class GrammarFragment : Fragment() {
    private lateinit var binding: FragmentGrammarBinding
    private val viewModel: FirestoreViewModel by activityViewModels()





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGrammarBinding.inflate(layoutInflater, container, false)
        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<TextView>(R.id.tv_titel).text = "GRAMMAR"

        // Daten im ViewModel beobachten und UI entsprechend aktualisieren
        viewModel.titles.observe(viewLifecycleOwner) { snapshot ->
            if (snapshot != null) {
                // Liste für die Grammatikdaten erstellen und füllen
                val titleList = mutableListOf<GrammatikData>()
                for (document in snapshot.documents) {
                    val title = document.toObject(GrammatikData::class.java)
                    titleList.add(title!!)
                }

                Log.e(TAG, "Retrieved: ${titleList.size} titles.")
                binding.rvGrammatik.adapter = GrammarAdapter(titleList)

            } else {
                Log.e(TAG, "Our data is null.")
            }

        }

        // Textänderungslistener für die Suchleiste einrichten
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
        })

    }
    private fun filter(text: String) {
        // Liste der Grammatikeinträge filtern und dem Adapter übergeben
        val filteredList = viewModel.grammatik.value?.filter { it.title.contains(text, ignoreCase = true) }
        (binding.rvGrammatik.adapter as GrammarAdapter).submitList(filteredList)
    }

}
