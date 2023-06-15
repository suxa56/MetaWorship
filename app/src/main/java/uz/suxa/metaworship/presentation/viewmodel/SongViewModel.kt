package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.usecase.GetSongUseCase

class SongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongUseCase = GetSongUseCase(repo)

    suspend fun getSong(songId: String) = getSongUseCase(songId)
}