package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.usecase.song.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.song.DownloadSongsUseCase
import uz.suxa.metaworship.domain.usecase.song.GetChordsUseCase
import uz.suxa.metaworship.domain.usecase.song.GetLyricsUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByQueryUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByVocalistUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongUseCase
import uz.suxa.metaworship.domain.usecase.song.UploadSongsUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.AddVocalistUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.GetVocalistWithSongCountUseCase
import java.util.UUID

class HomeViewModel(application: Application) : TonalityViewModel(application) {

    private val songRepo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(songRepo)
    private val getSongUseCase = GetSongUseCase(songRepo)
    private val getSongListByVocalist = GetSongListByVocalistUseCase(songRepo)
    private val getSongListByQuery = GetSongListByQueryUseCase(songRepo)
    private val deleteSongUseCase = DeleteSongUseCase(songRepo)
    private val getLyricsUseCase = GetLyricsUseCase(songRepo)
    private val getChordsUseCase = GetChordsUseCase(songRepo)
    private val uploadSongsUseCase = UploadSongsUseCase(songRepo)
    private val downloadSongsUseCase = DownloadSongsUseCase(songRepo)

    private val vocalistRepo = VocalistRepoImpl(application)
    private val addVocalistUseCase = AddVocalistUseCase(vocalistRepo)
    private val getVocalistWithSongCountUseCase = GetVocalistWithSongCountUseCase(vocalistRepo)

    private val _songs = MediatorLiveData<List<SongModel>>()
    val songs: LiveData<List<SongModel>> get() = _songs

    private val _vocalistsDto = MediatorLiveData<List<VocalistSongDto>>()
    val vocalistsDto: LiveData<List<VocalistSongDto>> get() = _vocalistsDto

    private var activeSource: LiveData<List<SongModel>>? = null

    private val _copy = MutableLiveData<String>()
    val copy: LiveData<String> get() = _copy

    private val _copySong = MutableLiveData<SongModel>()
    val copySong: LiveData<SongModel> get() = _copySong

    init {
        viewModelScope.launch {
            _songs.addSource(getSongList()) {
                _songs.value = it
            }
        }
    }

    fun uploadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            uploadSongsUseCase()
        }
    }

    fun downloadSongs() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadSongsUseCase()
        }
    }

    fun createVocalist(vocalistString: String): Boolean {
        if (vocalistString.isBlank()) {
            return false
        }
        val vocalist = VocalistModel(
            id = "voc_" + UUID.randomUUID().toString(),
            name = vocalistString
        )
        viewModelScope.launch(Dispatchers.IO) {
            addVocalistUseCase(vocalist)
        }
        return true
    }

    fun getVocalists() {
        viewModelScope.launch {
            _vocalistsDto.addSource(getVocalistWithSongCountUseCase()) {
                _vocalistsDto.value = it
            }
        }
    }

    fun getAllSongs() {
        viewModelScope.launch {
            clearSource()
            activeSource = getSongList()
            setSource()
        }
    }

    fun getSongsByQuery(query: String) {
        viewModelScope.launch {
            clearSource()
            activeSource = getSongListByQuery(query)
            setSource()
        }
    }

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = getSongUseCase(songId)
            _copySong.postValue(song)
        }
    }

    fun getLyrics(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val lyrics = getLyricsUseCase(songId)
            _copy.postValue(lyrics)
        }
    }

    fun getChords(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val lyrics = getChordsUseCase(songId)
            _copy.postValue(lyrics)
        }
    }

    fun getSongsByVocalist(vocalist: String) {
        viewModelScope.launch {
            clearSource()
            activeSource = getSongListByVocalist(vocalist)
            setSource()
        }
    }

    fun deleteSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSongUseCase(songId)
        }
    }

    private fun clearSource() {
        activeSource?.let { _songs.removeSource(it) }
    }

    private fun setSource() {
        _songs.addSource(activeSource!!) {
            _songs.value = it
        }
    }
}