package uz.suxa.metaworship.domain.model

data class SongModel(
    val id: String? = null,
    val title: String? = null,
    val lyrics: String? = null,
    val chords: String? = null,
    val defaultTonality: Tonality? = null,
    val modulations: List<String>? = null,
    val vocalistTonality: List<VocalistTonality>? = null,
    val tempo: Int? = null
)
