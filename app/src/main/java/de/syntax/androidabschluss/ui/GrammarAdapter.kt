package de.syntax.androidabschluss.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.models.GrammatikData
import de.syntax.androidabschluss.databinding.ItemGrammatikBinding

class GrammarAdapter(
    private var dataset: List<GrammatikData>


) : RecyclerView.Adapter<GrammarAdapter.TitleViewHolder>() {


    class TitleViewHolder(val binding: ItemGrammatikBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val binding =
            ItemGrammatikBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val title = dataset[position]

        holder.binding.wordTextView.text = title.title
        holder.binding.grammatikB.setOnClickListener {
            val action =
                GrammarFragmentDirections.actionGrammarFragmentToGrammarDetailFragment(title.id)
            Navigation.findNavController(it).navigate(action)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun submitList(list: List<GrammatikData>?) {
        // Datensatz aktualisieren, wenn er nicht null ist, andernfalls leere Liste verwenden
        dataset = list ?: emptyList()
        notifyDataSetChanged()
    }

}

