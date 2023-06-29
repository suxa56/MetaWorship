package uz.suxa.metaworship.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongDbModel(
    @PrimaryKey
    val id: String,
    val title: String,
    val lyrics: String,
    val defaultTonality: String,
    val chords: String,
    val modulations: String,
    val vocalist: String,
    val tonality: String,
    val soloPart: String,
    val solo: String,
    val tempo: Int,
    val lastUpdate: Long
)