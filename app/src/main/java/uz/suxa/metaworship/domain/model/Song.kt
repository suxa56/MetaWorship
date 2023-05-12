package uz.suxa.metaworship.domain.model

data class Song(
    val title: String,
    val lyrics: String,
    val chords: String,
    val tempo: Int? = null,
    val lastUpdate: String? = null,
    val vocalistTonality: VocalistsTonality? = null
)