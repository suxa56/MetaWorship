package uz.suxa.metaworship.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.usecase.AddSongUseCase

class SongViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)

    private val addSongUseCase = AddSongUseCase(repo)

    fun addSong(title: String, lyrics: String, chords: String, tonality: Tonality) {
        val song = SongModel(
            title = title,
            lyrics = lyrics,
            chords = chords,
            defaultTonality = tonality
        )

        viewModelScope.launch {
            addSongUseCase(song)
        }
    }
}