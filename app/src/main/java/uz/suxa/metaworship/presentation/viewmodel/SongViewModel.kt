package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.usecase.AddSongUseCase

class SongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)

    private val addSongUseCase = AddSongUseCase(repo)

    private val _tonalityList = MutableLiveData<List<String>>()
    val tonalityList: LiveData<List<String>> get() = _tonalityList

    init {
        fillTonalityList()
    }

    fun addSong(title: String, lyrics: String?, chords: String?, tonalityString: String?, tempo: String?, shouldClose: ShouldClose?) {
        val tonality = convertStringToTonality(tonalityString)
            viewModelScope.launch {
            val song = SongModel(
                title = title,
                lyrics = lyrics,
                chords = convertNotesToNumbers(tonality, chords),
                defaultTonality = tonality,
                tempo = getTempo(tempo)
            )
            addSongUseCase(song)
        }
        shouldClose?.onComplete()
    }

    private fun fillTonalityList() {
        _tonalityList.value = listOf(
            "C", "C#", "D", "Eb", "E", "F",
            "F#", "G", "Ab", "A", "Hb", "H"
        )
    }

    // TODO(): implement fun's convert chords to numbers

    private fun convertStringToTonality(tonality: String?): Tonality? {
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

    private fun getTempo(tempo: String?): Int? {
        return if (tempo.isNullOrBlank()) {
            null
        } else {
            tempo.toInt()
        }
    }

    interface ShouldClose {
        fun onComplete()
    }
}