package de.syntax.androidabschluss.util

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.models.Meaning
import de.syntax.androidabschluss.databinding.FragmentRegistierenBinding
import de.syntax.androidabschluss.databinding.MeaningRecycleViewBinding

class MeaningAdapter(private var meaningList: List<Meaning>) :
    RecyclerView.Adapter<MeaningAdapter.MeanigViewHolder>() {

    class MeanigViewHolder(private val binding: MeaningRecycleViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: Meaning) {
            //bin all meaning
            binding.tvPartOfSpeech.text = meaning.partOfSpeech
            binding.tvDefinition.text = meaning.definitions.joinToString("\n\n") {
                var currentIndex = meaning.definitions.indexOf(it)
                (currentIndex + 1).toString() + ". " + it.definition.toString()
            }
            // Überprüfe, ob Synonyme vorhanden sind
            if (meaning.synonyms.isEmpty()) {
                // Wenn keine Synonyme vorhanden sind, verstecke das entsprechende Textfeld
                binding.tvSynonymsTitle.visibility = View.GONE
                binding.tvSynonyms.visibility = View.GONE
            } else {
                // Wenn Synonyme vorhanden sind, zeige das entsprechende Textfeld und setze die Synonyme
                binding.tvSynonymsTitle.visibility = View.VISIBLE
                binding.tvSynonyms.visibility = View.VISIBLE
                binding.tvSynonyms.text = meaning.synonyms.joinToString(", ")
            }
            // Überprüfe, ob Antonyme vorhanden sind
            if (meaning.antonyms.isEmpty()) {
                // Wenn keine Antonyme vorhanden sind, verstecke das entsprechende Textfeld
                binding.tvAntonymsTitle.visibility = View.GONE
                binding.antonyms.visibility = View.GONE
            } else {
                // Wenn Antonyme vorhanden sind, zeige das entsprechende Textfeld und setze die Antonyme
                binding.tvAntonymsTitle.visibility = View.VISIBLE
                binding.antonyms.visibility = View.VISIBLE
                binding.antonyms.text = meaning.synonyms.joinToString(", ")
            }

        }
    }

    // Aktualisiere die Daten und benachrichtige den Adapter über die Änderungen
    @SuppressLint("NotifyDataSetChanged")
    fun updateNewData(newMeaningList: List<Meaning>) {
        meaningList = newMeaningList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeanigViewHolder {
        val binding =
            MeaningRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeanigViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return meaningList.size
    }

    override fun onBindViewHolder(holder: MeanigViewHolder, position: Int) {
        holder.bind(meaningList[position])
    }
}