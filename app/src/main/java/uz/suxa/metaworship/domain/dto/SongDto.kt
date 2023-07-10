package uz.suxa.metaworship.domain.dto

import uz.suxa.metaworship.domain.model.SoloPart
import uz.suxa.metaworship.domain.model.VocalistTonality

data class SongDto(
    val title: String?,
    val lyrics: String?,
    val chords: String?,
    val defaultTonality: String?,
    val modulations: List<String>?,
    val vocalistTonality: List<VocalistTonality>?,
    val soloPart: List<SoloPart>?,
    val tempo: String?
)
