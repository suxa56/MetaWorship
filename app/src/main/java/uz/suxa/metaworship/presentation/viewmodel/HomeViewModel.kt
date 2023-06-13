package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.usecase.GetSongListUseCase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(repo)

    suspend fun getSongs() = getSongList()
}