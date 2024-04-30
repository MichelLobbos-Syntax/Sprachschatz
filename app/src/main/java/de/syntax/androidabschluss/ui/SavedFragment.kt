package de.syntax.androidabschluss.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {

    private lateinit var binding: FragmentSavedBinding

    private val viewModel: SavedViewModel by activityViewModels()

    private lateinit var savedAdapter: SavedAdapter

    // Flag, um zu überprüfen, ob alle Elemente angezeigt werden sollen
    private var all: Boolean = false

    // Callback für das Swipen zum Löschen von Einträgen
    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        // Methode, um festzulegen, ob die Elemente verschoben werden können
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        // Methode, um auf das Swipen zu reagieren
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val savedAdapter = binding.rvGespeichert.adapter as? SavedAdapter

            // AlertDialog zum Bestätigen des Löschvorgangs
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.apply {
                setTitle("Delete translation") // Titel des Dialogs
                setMessage("Are you sure you want to delete this translation?") // Nachricht im Dialog
                setPositiveButton("Yes") { dialog, _ ->
                    val deletedItem = savedAdapter?.getItem(position)
                    deletedItem?.let { viewModel.delete(it) }
                    savedAdapter?.removeItem(position)
                    dialog.dismiss()
                }
                setNegativeButton("No") { dialog, _ ->
                    savedAdapter?.notifyItemChanged(position) // Zurücksetzen des Swipes
                    dialog.dismiss()
                }
                create().show() // Anzeigen des Dialogs
            }
        }
    }










    // Methode zur Erstellung und Inflation des Fragment-Layouts
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBinding.inflate(layoutInflater)
        return binding.root
    }

    // Methode, die nach der Erstellung des Fragment-Views aufgerufen wird
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Titel des Fragments setzen
        requireActivity().findViewById<TextView>(R.id.tv_titel).text = "SAVED"

        // Beobachten der LiveData für gespeicherte Daten und Aktualisieren der RecyclerView
        viewModel.gespeichertList.observe(viewLifecycleOwner) { gespeichert ->
            val reversedList = gespeichert.reversed()
            savedAdapter = SavedAdapter(requireContext(), reversedList.toMutableList())

            binding.rvGespeichert.adapter = savedAdapter

            val swipeToDeleteCallback = SwipeToDeleteCallback()
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(binding.rvGespeichert)
        }
    }


}
