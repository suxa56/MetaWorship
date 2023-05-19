package uz.suxa.metaworship.domain.model

data class SongModel(
    val title: String,
    val lyrics: String? = null,
    val chords: String? = null,
    val vocalistTonality: List<VocalistTonality>? = null,
    val tempo: Int? = null
)
