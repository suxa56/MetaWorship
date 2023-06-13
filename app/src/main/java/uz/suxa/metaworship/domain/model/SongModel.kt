package uz.suxa.metaworship.domain.model

data class SongModel(
    val id: String? = null,
    val title: String,
    val lyrics: String,
    val chords: String,
    val defaultTonality: Tonality,
    val modulations: List<String>,
    val vocalistTonality: List<VocalistTonality>,
    val tempo: Int
)
