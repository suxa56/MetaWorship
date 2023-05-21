package uz.suxa.metaworship.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongDbModel(
    @PrimaryKey
    val id: String,
    val title: String,
    val lyrics: String? = null,
    val chords: String? = null,
    val vocalist: String? = null,
    val tonality: String? = null,
    val tempo: Int? = null,
    val lastUpdate: Long
)