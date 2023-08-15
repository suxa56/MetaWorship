package uz.suxa.metaworship.data

import uz.suxa.metaworship.data.db.SongDbModel
import uz.suxa.metaworship.data.db.SongDbModelDto
import uz.suxa.metaworship.domain.model.SoloPart
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.model.VocalistTonality
import java.util.Calendar

class SongMapper {

    fun mapEntityToDbModel(song: SongModel) = SongDbModel(
        id = song.id,
        title = song.title,
        lyrics = song.lyrics,
        chords = song.chords,
        defaultTonality = song.defaultTonality.toString(),
        modulations = mapModulationsToString(song.modulations),
        vocalist = mapVocalistTonalityToVocalistString(song.vocalistTonality),
        tonality = mapVocalistTonalityToTonalityString(song.vocalistTonality),
        soloPart = mapSoloPartToPartString(song.soloPart),
        solo = mapSoloPartToSoloString(song.soloPart),
        tempo = song.tempo,
        lastUpdate = Calendar.getInstance().timeInMillis
    )

    fun mapDbModelToEntity(songDb: SongDbModel) = SongModel(
        id = songDb.id,
        title = songDb.title,
        lyrics = songDb.lyrics,
        chords = songDb.chords,
        defaultTonality = mapStringToTonality(songDb.defaultTonality),
        modulations = songDb.modulations.split(SEPARATOR),
        vocalistTonality = mapStringToVocalistTonality(songDb.vocalist, songDb.tonality),
        soloPart = mapStringToSoloPart(songDb.soloPart, songDb.solo),
        tempo = songDb.tempo,
    )

    fun mapFirebaseToDbModel(data: Map<String, Any?>) = SongDbModel(
        id = data["id"] as String,
        title = data["title"] as String,
        lyrics = data["lyrics"] as String,
        defaultTonality = data["defaultTonality"] as String,
        chords = data["chords"] as String,
        modulations = data["modulations"] as String,
        vocalist = data["vocalist"] as String,
        tonality = data["tonality"] as String,
        soloPart = data["soloPart"] as String,
        solo = data["solo"] as String,
        tempo = (data["tempo"] as Long).toInt(),
        lastUpdate = data["lastUpdate"] as Long
    )

    fun mapListDbModelDtoToListEntity(list: List<SongDbModelDto>) = list.map {
        mapDbModelDtoToEntityDto(it)
    }

    private fun mapDbModelDtoToEntityDto(songDb: SongDbModelDto) = SongModel(
        id = songDb.id,
        title = songDb.title,
        lyrics = songDb.lyrics,
        chords = "",
        defaultTonality = mapStringToTonality(songDb.defaultTonality),
        modulations = listOf<String>(),
        vocalistTonality = mapStringToVocalistTonality(songDb.vocalist, songDb.tonality),
        soloPart = listOf<SoloPart>(),
        tempo = -1,
    )

    private fun mapModulationsToString(modulations: List<String>) =
        modulations.joinToString(SEPARATOR) {
            it
        }

    private fun mapVocalistTonalityToVocalistString(vocalistTonality: List<VocalistTonality>) =
        vocalistTonality.joinToString(SEPARATOR) {
            it.vocalist
        }

    private fun mapVocalistTonalityToTonalityString(vocalistTonality: List<VocalistTonality>) =
        vocalistTonality.joinToString(SEPARATOR) {
            it.tonality
        }

    private fun mapSoloPartToPartString(soloPart: List<SoloPart>) =
        soloPart.joinToString(SEPARATOR) {
            it.part
        }

    private fun mapSoloPartToSoloString(soloPart: List<SoloPart>) =
        soloPart.joinToString(SEPARATOR) {
            it.solo
        }

    private fun mapStringToVocalistTonality(
        vocalist: String?,
        tonality: String?
    ): List<VocalistTonality> {
        if (vocalist.isNullOrBlank() || tonality.isNullOrBlank()) {
            return listOf()
        }
        val vocalistTonalityList = mutableListOf<VocalistTonality>()

        val vocalistArray = vocalist.split(SEPARATOR).toTypedArray()
        val tonalityArray = tonality.split(SEPARATOR).toTypedArray()

        for ((index, _) in vocalistArray.withIndex()) {
            vocalistTonalityList.add(
                VocalistTonality(
                    vocalistArray[index],
                    tonalityArray[index]
                )
            )
        }

        return vocalistTonalityList
    }

    private fun mapStringToSoloPart(
        part: String?,
        solo: String?
    ): List<SoloPart> {
        if (part.isNullOrBlank() || solo.isNullOrBlank()) {
            return listOf()
        }
        val soloPartList = mutableListOf<SoloPart>()

        val partArray = part.split(SEPARATOR).toTypedArray()
        val soloArray = solo.split(SEPARATOR).toTypedArray()

        for ((index, _) in partArray.withIndex()) {
            soloPartList.add(
                SoloPart(
                    partArray[index],
                    soloArray[index]
                )
            )
        }

        return soloPartList
    }

    private fun mapStringToTonality(tonality: String): Tonality {
        return if (tonality.isBlank()) {
            Tonality.UNDEFINED
        } else {
            Tonality.valueOf(tonality)
        }
    }

    companion object {
        private const val SEPARATOR = ","
    }
}