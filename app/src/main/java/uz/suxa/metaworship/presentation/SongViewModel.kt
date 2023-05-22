package uz.suxa.metaworship.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.usecase.AddSongUseCase

class SongViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)

    private val addSongUseCase = AddSongUseCase(repo)

    private val _tonalityList = MutableLiveData<List<String>>()
    val tonalityList: LiveData<List<String>> get() = _tonalityList

    init {
        fillTonalityList()
    }

    fun addSong(title: String, lyrics: String, chords: String, tonality: Tonality) =
        viewModelScope.launch {
            val song = SongModel(
                title = title,
                lyrics = lyrics,
                chords = chords,
                defaultTonality = tonality
            )
            addSongUseCase(song)
        }

    private fun fillTonalityList() {
        _tonalityList.value = listOf(
            "C", "C#", "Db", "D", "D#", "Eb", "E", "F",
            "Gb", "G", "G#", "Ab", "A", "A#", "B/Hb", "H"
        )
    }



}