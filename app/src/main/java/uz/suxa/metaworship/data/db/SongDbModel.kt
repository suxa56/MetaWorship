package uz.suxa.metaworship.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.suxa.metaworship.domain.model.VocalistTonality

@Entity(tableName = "songs")
data class SongDBModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val lyrics: String? = null,
    val chords: String? = null,
    val vocalistTonality: List<VocalistTonality>? = null,
    val tempo: Int? = null,
    val lastUpdate: Long
)