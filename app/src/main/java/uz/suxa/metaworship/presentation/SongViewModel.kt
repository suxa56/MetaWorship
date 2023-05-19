package uz.suxa.metaworship.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.usecase.AddSongUseCase

class SongViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl()

    private val addSongUseCase = AddSongUseCase(repo)

    fun addSong(title: String, lyrics: String, chords: String) {
        val song = SongModel(title, lyrics, chords)
        viewModelScope.launch {
            addSongUseCase(song)
        }
    }
}