package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.usecase.GetSongUseCase

class SongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongUseCase = GetSongUseCase(repo)

    private val _song = MutableLiveData<SongModel>()
    val song: LiveData<SongModel> get() = _song

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _song.postValue(getSongUseCase.invoke(songId))
        }
    }
}