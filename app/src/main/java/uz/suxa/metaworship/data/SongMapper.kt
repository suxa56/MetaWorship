package uz.suxa.metaworship.data

import uz.suxa.metaworship.data.db.SongDbModel
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.model.VocalistTonality
import java.util.*

class SongMapper {

    fun mapEntityToDbModel(song: SongModel) = SongDbModel(
        id = song.id ?: UUID.randomUUID().toString(),
        title = song.title,
        lyrics = song.lyrics,
        chords = song.chords,
        defaultTonality = song.defaultTonality.toString(),
        modulations = mapModulationsToString(song.modulations),
        vocalist = mapVocalistTonalityToVocalistString(song.vocalistTonality),
        tonality = mapVocalistTonalityToTonalityString(song.vocalistTonality),
        tempo = song.tempo,
        lastUpdate = Calendar.getInstance().timeInMillis
    )

    fun mapDbModelToEntity(songDb: SongDbModel) = SongModel(
        id = songDb.id,
        title = songDb.title,
        lyrics = songDb.lyrics,
        chords = songDb.chords,
        defaultTonality = songDb.defaultTonality?.let { Tonality.valueOf(it) },
        vocalistTonality = mapStringToVocalistTonality(songDb.vocalist, songDb.tonality),
        tempo = songDb.tempo,
    )

    fun mapListDbModelToListEntity(list: List<SongDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

    private fun mapModulationsToString(modulations: List<String>?) =
        modulations?.joinToString(SEPARATOR) {
            it
        }

    private fun mapVocalistTonalityToVocalistString(vocalistTonality: List<VocalistTonality>?) =
        vocalistTonality?.joinToString(SEPARATOR) {
            it.vocalist
        }

    private fun mapVocalistTonalityToTonalityString(vocalistTonality: List<VocalistTonality>?) =
        vocalistTonality?.joinToString(SEPARATOR) {
            it.tonality.toString()
        }

    private fun mapStringToVocalistTonality(
        vocalist: String?,
        tonality: String?
    ): List<VocalistTonality>? {
        if (vocalist == null || tonality == null) {
            return null
        }
        val vocalistTonalityList = mutableListOf<VocalistTonality>()

        val vocalistArray = vocalist.split(SEPARATOR).toTypedArray()
        val tonalityArray = tonality.split(SEPARATOR).toTypedArray()

        for ((index, _) in vocalistArray.withIndex()) {
            vocalistTonalityList.add(
                VocalistTonality(
                    vocalistArray[index],
                    Tonality.valueOf(tonalityArray[index])
                )
            )
        }

        return vocalistTonalityList
    }

    companion object {
        private const val SEPARATOR = ","
    }
}