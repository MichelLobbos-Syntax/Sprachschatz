package de.syntax.androidabschluss.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Saved(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val input: String,
    val output: String
)
