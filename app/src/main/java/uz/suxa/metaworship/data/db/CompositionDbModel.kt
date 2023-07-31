package uz.suxa.metaworship.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "compositions")
data class CompositionDbModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val songId: String,
    val tonality: String,
    val creationDate: Long
)
