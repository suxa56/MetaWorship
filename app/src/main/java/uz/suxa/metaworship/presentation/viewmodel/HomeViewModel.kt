package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.usecase.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.GetSongListUseCase
import uz.suxa.metaworship.domain.usecase.GetVocalistWithSongCountUseCase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(repo)
    private val deleteSongUseCase = DeleteSongUseCase(repo)

    private val _songs = MediatorLiveData<List<SongModel>>()
    val songs: LiveData<List<SongModel>> get() = _songs

    private val vocalistRepo = VocalistRepoImpl(application)
    private val getVocalistWithSongCountUseCase = GetVocalistWithSongCountUseCase(vocalistRepo)

    private val _vocalistsDto = MediatorLiveData<List<VocalistSongDto>>()
    val vocalistsDto: LiveData<List<VocalistSongDto>> get() = _vocalistsDto

    init {
        viewModelScope.launch {
            _songs.addSource(getSongList()) {
                _songs.value = it
            }
        }
    }

    fun getVocalists() {
        viewModelScope.launch {
            _vocalistsDto.addSource(getVocalistWithSongCountUseCase()) {
                _vocalistsDto.value = it
            }
        }
    }

    fun deleteSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSongUseCase(songId)
        }
    }
}