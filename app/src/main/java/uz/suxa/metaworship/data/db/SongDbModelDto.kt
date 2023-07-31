package uz.suxa.metaworship.data.db

data class SongDbModelDto(
    val id: String,
    val title: String,
    val lyrics: String,
    val defaultTonality: String,
    val chords: String,
    val vocalist: String,
    val tonality: String,
)
