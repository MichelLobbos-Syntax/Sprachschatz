package de.syntax.androidabschluss.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import de.syntax.androidabschluss.data.local.SavedDatabase
import de.syntax.androidabschluss.data.models.Saved


const val TAG = "Repository"
class GespeichertRepository(private val database: SavedDatabase) {


    val savedList: LiveData<List<Saved>> = database.dao.getAll()

    /**
     * Methode zum Einfügen eines gespeicherten Eintrags in die Datenbank.
     * @param saved Der zu speichernde Eintrag.
     */
    suspend fun insert(saved: Saved) {
        try {
            database.dao.insert(saved)
        } catch (e: Exception) {
            Log.e(TAG, "Error writing into database: $e")
        }
    }

    /**
     * Methode zum Aktualisieren eines gespeicherten Eintrags in der Datenbank.
     * @param saved Der zu aktualisierende Eintrag.
     */
    suspend fun update(saved: Saved) {
        try {
            database.dao.update(saved)
        } catch (e: Exception) {
            Log.e(TAG, "Error updating database: $e")
        }
    }

    /**
     * Methode zum Löschen eines gespeicherten Eintrags aus der Datenbank.
     * @param saved Der zu löschende Eintrag.
     */
    suspend fun delete(saved: Saved) {
        try {
            database.dao.deleteById(saved.id)
        } catch (e: Exception) {
            // Fehler beim Löschen aus der Datenbank
            Log.e(TAG, "Error deleting from database: $e")
        }
    }

}