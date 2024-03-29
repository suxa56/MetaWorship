package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality

abstract class TonalityViewModel(application: Application) : AndroidViewModel(application) {
    private val sharpTonalities = listOf(
        Tonality.C, Tonality.C_SHARP, Tonality.D, Tonality.E,
        Tonality.F_SHARP, Tonality.G, Tonality.A, Tonality.H
    )

//    private val flatTonalities = listOf(
//        Tonality.E_FLAT, Tonality.F,
//        Tonality.A_FLAT, Tonality.H_FLAT
//    )

    private val sharpNotes = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "H")

    private val flatNotes = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Hb", "H")

    private val tonalitiesList =
        listOf("C", "C#", "D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Hb", "H")

    fun copySong(song: SongModel, tonality: Tonality): CharSequence {
        var text = song.title + ": " + convertTonalityToSymbol(tonality) + "\n"
        text += song.lyrics + "\n\n"
        text += convertNumbersToNotes(tonality, song.chords)
        return text
    }

    fun copySongChords(title: String, tonality: Tonality, chords: String): CharSequence {
        var text = title + ": " + convertTonalityToSymbol(tonality) + "\n"
        text += convertNumbersToNotes(tonality, chords)
        return text
    }

    fun copySongLyrics(title: String, lyrics: String): CharSequence {
        var text = title + "\n"
        text += lyrics
        return text
    }

    // Replace all chords and notes in incoming string to numbers
    fun convertNotesToNumbers(tonality: Tonality, chords: String): String {
        if (tonality == Tonality.UNDEFINED || chords.isBlank()) {
            return ""
        }
        val notes = getTonalityNotes(tonality)
        val numberedNotes = convertListToNumberedMap(notes)
        var converted = chords.uppercase()
        converted = converted.replace("B", "b")

        // For first checks non-Tonality notes, then check Tonality notes
        // After every iteration save result to variable
        numberedNotes.forEach {
            it.forEach { (index, value) ->
                converted = converted.replace(value, "-$index-")
            }
        }
        converted = converted.replace(" ", "-")

        return converted

    }

    fun convertNumbersToNotes(tonality: Tonality, chords: String): String {
        if (tonality == Tonality.UNDEFINED || chords.isBlank()) {
            return ""
        }
        val notes = getTonalityNotes(tonality)
        val numberedNotes = convertListToNumberedMap(notes)
        var converted = chords.replace("M", "m")

        numberedNotes.forEach {
            it.forEach { (index, value) ->
                converted = converted.replace("-$index-", value)
            }
        }
        converted = converted.replace("-", " ")
        converted = converted.replace("B", "b")

        return converted
    }

    fun convertModulationToString(tonality: Tonality, modulation: List<String>): List<String> {
        if (modulation.isEmpty() || tonality == Tonality.UNDEFINED) {
            return listOf("")
        }
        val correctNotesList = arrayListOf<String>()
        val fragment = arrayListOf<String>()
        var startAnotherList = false
        tonalitiesList.map { c ->
            if (c == convertTonalityToSymbol(tonality) || startAnotherList) {
                startAnotherList = true
                correctNotesList.add(c)
            } else {
                fragment.add(c)
            }
        }
        correctNotesList.addAll(fragment)

        val numberedNotes = convertListToNumberedMap(correctNotesList)

        var converted = modulation.joinToString(",") {
            it
        }

        numberedNotes.forEach {
            it.forEach { (index, value) ->
                converted = converted.replace(value, index)
            }
        }
        return converted.split(",").toList()
    }

    fun convertStringToModulation(tonality: Tonality, modulation: List<String>): List<String> {
        if (modulation.isEmpty() || tonality == Tonality.UNDEFINED) {
            return listOf("")
        }
        val correctNotesList = arrayListOf<String>()
        val fragment = arrayListOf<String>()
        var startAnotherList = false
        tonalitiesList.map { c ->
            if (c == convertTonalityToSymbol(tonality) || startAnotherList) {
                startAnotherList = true
                correctNotesList.add(c)
            } else {
                fragment.add(c)
            }
        }
        correctNotesList.addAll(fragment)

        val numberedNotes = convertListToNumberedMap(correctNotesList)

        var converted = modulation.joinToString(",") {
            it
        }

        numberedNotes.forEach {
            it.forEach { (index, value) ->
                converted = converted.replace(index, value)
            }
        }
        return converted.split(",").toList()
    }

    // Build right notes queue with its number
    private fun getTonalityNotes(tonality: Tonality): List<String> {
        val notes = if (sharpTonalities.contains(tonality)) {
            sharpNotes
        } else {
            flatNotes
        }
        val correctNotesList = arrayListOf<String>()
        val fragment = arrayListOf<String>()
        var startAnotherList = false
        notes.map { c ->
            if (c == convertTonalityToSymbol(tonality) || startAnotherList) {
                startAnotherList = true
                correctNotesList.add(c)
            } else {
                fragment.add(c)
            }
        }
        correctNotesList.addAll(fragment)
        return correctNotesList
    }

    // Convert list to maps(non-Tonality, Tonality) with numbers in index
    private fun convertListToNumberedMap(list: List<String>): List<Map<String, String>> {
        val mapNatural = HashMap<String, String>()
        val mapSymbol = HashMap<String, String>()
        var i = 1
        list.forEach {
            if (it.contains("#") || it.contains("b")) {
                mapSymbol[i++.toString()] = it
            } else {
                mapNatural[i++.toString()] = it
            }
        }
        return listOf(mapSymbol, mapNatural)
    }

    fun getTonalityPosition(tonalityString: String) = tonalitiesList.indexOf(tonalityString)

    fun getTonality(position: Int) = tonalitiesList[position]

    // Convert tonality to symbols -> C_SHARP == C#
    fun convertTonalityToSymbol(tonality: Tonality) =
        tonality.toString()
            .replace("_SHARP", "#").replace("_FLAT", "b")

    fun convertStringToTonality(tonality: String?): Tonality {
        return if (tonality.isNullOrBlank()) {
            Tonality.UNDEFINED
        } else {
            val refactoredTonality =
                tonality
                    .replace("#", "_SHARP")
                    .replace("b", "_FLAT")
                    .replace(" ", "")

            Tonality.valueOf(refactoredTonality)
        }
    }
}