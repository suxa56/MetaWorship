package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.usecase.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.GetSongListUseCase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(repo)
    private val deleteSongUseCase = DeleteSongUseCase(repo)

    suspend fun getSongs() = getSongList()

    fun deleteSong(song: SongModel) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSongUseCase(song)
        }
    }
}