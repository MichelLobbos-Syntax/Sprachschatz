package de.syntax.androidabschluss.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.models.Saved
import de.syntax.androidabschluss.databinding.ItemSaveBinding


class SavedAdapter(private val context: Context, private val dataset: MutableList<Saved>) :
    RecyclerView.Adapter<SavedAdapter.GuestViewHolder>() {

    class GuestViewHolder(val binding: ItemSaveBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemSaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val gespeichert = dataset[position]

        holder.binding.tvInput.text = gespeichert.input
        holder.binding.tvOutput.text = gespeichert.output

        holder.binding.ibCopie.setOnClickListener {
            val textToCopy = gespeichert.output

            // Zugriff auf den System-Clipboard
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Erstellen einer ClipData mit dem zu kopierenden Text
            val clipData = ClipData.newPlainText("Text", textToCopy)

            // Setzen der ClipData ins Clipboard
            clipboardManager.setPrimaryClip(clipData)

            // Benachrichtigung an den Benutzer, dass der Text kopiert wurde
            //Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }

    }

    // Methode zum Abrufen eines Elements an einer bestimmten Position
    fun getItem(position: Int): Saved {
        return dataset[position]
    }

    // Methode zum Entfernen eines Elements an einer bestimmten Position
    fun removeItem(position: Int) {
        dataset.removeAt(position)
        notifyItemRemoved(position)
    }
}
