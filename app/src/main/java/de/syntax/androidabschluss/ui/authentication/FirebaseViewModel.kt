package de.syntax.androidabschluss.ui.authentication

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import de.syntax.androidabschluss.data.models.GrammatikData

const val TAG = "FirebaseViewModel"
class FirestoreViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // LiveData für den aktuellen Benutzer
    // currentuser ist null wenn niemand eingeloggt ist
    private val _currentUser = MutableLiveData<FirebaseUser?>(firebaseAuth.currentUser)
    val currentUser: LiveData<FirebaseUser?>
        get() = _currentUser

    // LiveData für die abgerufenen Titel aus Firestore
    private val _titles = MutableLiveData<QuerySnapshot>()
    val titles: LiveData<QuerySnapshot>
        get() = _titles

    private val titlesDb = db.collection("GrammatikData")

    // LiveData für die abgerufenen Grammatikdaten aus Firestore
    private val _grammatik = MutableLiveData<List<GrammatikData>>()
    val grammatik: LiveData<List<GrammatikData>>
        get()= _grammatik





    // Konstruktor, der den Snapshot-Listener initialisiert, um Änderungen an den Titeln zu überwachen
    init {
        titlesDb.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Listen failed: $error")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                _titles.value = snapshot

                val grammatikList = snapshot.toObjects(GrammatikData::class.java)
                _grammatik.value = grammatikList
            } else {
                Log.e(TAG,"Current data is null.")
            }

        }
    }




    // Methode zum Registrieren eines neuen Benutzers
    fun signup(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { user ->
            if (user.isSuccessful) {
                Log.e(TAG, "Signup isSuccessful: ${user}")

                login(email, password)


            } else {
                Log.e(TAG, "Signup failed: ${user.exception}")
            }
        }
    }


    // Methode zum Einloggen eines Benutzers
    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e(TAG, "Signin isSuccessful: ${_currentUser}")
                _currentUser.value = firebaseAuth.currentUser
            } else {
                Log.e(TAG, "Login failed: ${it.exception}")
                Toast.makeText(getApplication(), "Incorrect email or password. Please try again.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // Methode zum Abmelden des aktuellen Benutzers
    fun logout() {
        firebaseAuth.signOut()
        _currentUser.value = firebaseAuth.currentUser
    }


}