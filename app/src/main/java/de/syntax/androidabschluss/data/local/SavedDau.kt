package de.syntax.androidabschluss.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import de.syntax.androidabschluss.data.models.Saved

@Dao
interface SavedDau {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(saved: Saved)

    @Update
    suspend fun update(saved: de.syntax.androidabschluss.data.models.Saved)


    @Query("SELECT * FROM Saved")
    fun getAll(): LiveData<List<Saved>>


    @Query("DELETE FROM Saved")
    suspend fun deleteAll()

    @Query("DELETE FROM Saved WHERE id = :id")
    suspend fun deleteById(id: Long)
}