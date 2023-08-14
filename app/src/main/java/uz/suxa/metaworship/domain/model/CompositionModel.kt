package uz.suxa.metaworship.domain.model

data class CompositionModel(
    val id: String,
    val name: String,
    val songId: List<String>,
    val tonality: List<String>,
    val creationDate: Long
)
