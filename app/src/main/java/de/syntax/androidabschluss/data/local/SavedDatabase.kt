package de.syntax.androidabschluss.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.syntax.androidabschluss.data.models.Saved

@Database(entities = [Saved::class], version = 1)
abstract class SavedDatabase : RoomDatabase() {
    abstract val dao: SavedDau

}
    private lateinit var INSTANCE: SavedDatabase


    fun getDatabase(context: Context): SavedDatabase {

        synchronized(SavedDatabase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    SavedDatabase::class.java,
                    "saved_database"
                ).build()
            }
            return INSTANCE
        }

    }
