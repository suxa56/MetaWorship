package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import uz.suxa.metaworship.domain.model.Tonality

abstract class TonalityViewModel(application: Application) : AndroidViewModel(application) {
    val sharpTonalities = listOf(
        Tonality.C, Tonality.C_SHARP, Tonality.D, Tonality.E,
        Tonality.F_SHARP, Tonality.G, Tonality.A, Tonality.H
    )

    val flatTonalities = listOf(
        Tonality.E_FLAT, Tonality.F,
        Tonality.A_FLAT, Tonality.H_FLAT
    )

    val sharpNotes = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "H")

    val flatNotes = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Hb", "H")

    fun convertStringToTonality(tonality: String?): Tonality? {
        return if (tonality.isNullOrBlank()) {
            null
        } else {
            val refactoredTonality =
                tonality
                    .replace("#", "_SHARP")
                    .replace("b", "_FLAT")

            Tonality.valueOf(refactoredTonality)
        }
    }

    // TODO(): create fun's convert chords to numbers and reverse
    // Replace all chords and notes in incoming string to numbers
    fun convertNotesToNumbers(tonality: Tonality?, chords: String?): String {
        if (tonality == null || chords.isNullOrEmpty()) {
            return ""
        }
        val notes = getTonalityNotes(tonality)
        val numberedNotes = convertListToNumberedMap(notes)
        var converted = chords

        // For first checks non-Tonality notes, then check Tonality notes
        // After every iteration save result to variable
        numberedNotes.forEach {
            it.forEach { (index, value) ->
                converted = converted!!.replace(value, index)
            }
        }
        return converted!!

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

    // Convert tonality to symbols -> C_SHARP == C#
    private fun convertTonalityToSymbol(tonality: Tonality) =
        tonality.toString()
            .replace("_SHARP", "#").replace("_FLAT", "b")
}