package uz.suxa.metaworship.domain.model

data class SongModel(
    val id: String = UNDEFINED_ID,
    val title: String,
    val lyrics: String,
    val chords: String,
    val defaultTonality: Tonality,
    val modulations: List<String>,
    val vocalistTonality: List<VocalistTonality>,
    val tempo: Int
) {
    companion object {
        private const val UNDEFINED_ID = ""
    }
}
