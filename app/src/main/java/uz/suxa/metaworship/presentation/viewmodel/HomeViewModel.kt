package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.usecase.GetSongListUseCase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(repo)

//    private val _songList: MutableLiveData<List<SongModel>>()
//    val songList: LiveData<List<SongModel>?> get() = _songList

//    suspend fun lol() {
//        _songList.value = getSongList()
//    }

    suspend fun getSongs() = getSongList.invoke()
}