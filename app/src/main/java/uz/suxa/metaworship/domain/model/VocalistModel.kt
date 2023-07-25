package uz.suxa.metaworship.domain.model

data class VocalistModel(
    val id: String = UNDEFINED_ID,
    val name: String
) {
    companion object {
        private const val UNDEFINED_ID = ""
    }
}