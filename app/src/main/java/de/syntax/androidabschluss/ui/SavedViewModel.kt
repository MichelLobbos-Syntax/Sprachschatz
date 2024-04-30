package de.syntax.androidabschluss.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.local.getDatabase
import de.syntax.androidabschluss.data.models.Saved
import de.syntax.androidabschluss.data.repo.GespeichertRepository
import kotlinx.coroutines.launch

class SavedViewModel(application: Application) : AndroidViewModel(application) {
    // Datenbank-Instanz erhalten
    private val database = getDatabase(application)
    // Repository für den Zugriff auf gespeicherte Daten
    private val repository = GespeichertRepository(database)
    // LiveData-Objekt für die gespeicherte Liste
    val gespeichertList = repository.savedList

    // LiveData-Objekt für den Abschluss von Aktionen
    private val _complete = MutableLiveData<Boolean>()
    val complete: LiveData<Boolean>
        get() = _complete

    // Methode zum Einfügen von gespeicherten Daten
    fun insertGespeichert(saved: Saved) {
        viewModelScope.launch {
            repository.insert(saved)
            _complete.value = true
        }
    }

    // Methode zum Löschen von gespeicherten Daten
    fun delete(saved: Saved) {
        viewModelScope.launch {
            Log.e("GuestViewModel", "Deleted user with id: ${saved.id}")
            repository.delete(saved)
            _complete.value = true
        }
    }


}