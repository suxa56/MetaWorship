package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.usecase.song.AddSongUseCase
import uz.suxa.metaworship.domain.usecase.song.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.song.GetChordsUseCase
import uz.suxa.metaworship.domain.usecase.song.GetLyricsUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByQueryUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByVocalistUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongUseCase
import uz.suxa.metaworship.domain.usecase.song.SyncSongUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.AddVocalistUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.DeleteVocalistUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.GetVocalistListUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.SyncVocalistsUseCase
import java.util.UUID

class HomeViewModel(application: Application) : TonalityViewModel(application) {

    private val songRepo = SongRepoImpl(application)
    private val addSongUseCase = AddSongUseCase(songRepo)
    private val getSongList = GetSongListUseCase(songRepo)
    private val getSongUseCase = GetSongUseCase(songRepo)
    private val getSongListByVocalist = GetSongListByVocalistUseCase(songRepo)
    private val getSongListByQuery = GetSongListByQueryUseCase(songRepo)
    private val deleteSongUseCase = DeleteSongUseCase(songRepo)
    private val getLyricsUseCase = GetLyricsUseCase(songRepo)
    private val getChordsUseCase = GetChordsUseCase(songRepo)
    private val syncSongsUseCase = SyncSongUseCase(songRepo)

    private val vocalistRepo = VocalistRepoImpl(application)
    private val addVocalistUseCase = AddVocalistUseCase(vocalistRepo)
    private val getVocalistUseCase = GetVocalistListUseCase(vocalistRepo)
    private val deleteVocalistUseCase = DeleteVocalistUseCase(vocalistRepo)
    private val syncVocalistsUseCase = SyncVocalistsUseCase(vocalistRepo)

    private val _songs = MediatorLiveData<List<SongModel>>()
    val songs: LiveData<List<SongModel>> get() = _songs
    private val _searchSongs = MediatorLiveData<List<SongModel>>()
    val searchSongs: LiveData<List<SongModel>> get() = _searchSongs

    private val _vocalistsDto = MediatorLiveData<List<VocalistModel>>()
    val vocalistsDto: LiveData<List<VocalistModel>> get() = _vocalistsDto

    private var activeSource: LiveData<List<SongModel>>? = null

    private val _copy = MutableLiveData<String>()
    val copy: LiveData<String> get() = _copy

    private val _copySong = MutableLiveData<SongModel>()
    val copySong: LiveData<SongModel> get() = _copySong

    private val _refreshing = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> get() = _refreshing

    init {
        viewModelScope.launch {
            _songs.addSource(getSongList()) {
                _songs.value = it
            }
        }
    }

    fun syncCloud() {
        if (Firebase.auth.currentUser == null) {
            Firebase.auth.signInAnonymously().addOnCompleteListener {
                viewModelScope.launch(Dispatchers.IO) {
                    _refreshing.postValue(true)
                    syncSongsUseCase()
                    syncVocalistsUseCase()
                    _refreshing.postValue(false)
                }
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                _refreshing.postValue(true)
                syncSongsUseCase()
                syncVocalistsUseCase()
                _refreshing.postValue(false)
            }
        }
    }

    fun createVocalist(id: String?, vocalistString: String): Boolean {
        if (vocalistString.isBlank()) {
            return false
        }
        val vocalist = VocalistModel(
            id = id ?: ("voc_" + UUID.randomUUID().toString()),
            name = vocalistString
        )
        viewModelScope.launch(Dispatchers.IO) {
            addVocalistUseCase(vocalist)
        }
        return true
    }

    fun getVocalists() {
        viewModelScope.launch {
            _vocalistsDto.addSource(getVocalistUseCase()) {
                _vocalistsDto.value = it
            }
        }
    }

    fun deleteVocalist(vocalistId: String) {
        viewModelScope.launch {
            deleteVocalistUseCase(vocalistId)
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
            _searchSongs.addSource(getSongListByQuery(query)) {
                _searchSongs.value = it
            }
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

    fun editTonality(songId: String, tonality: Tonality) {
        viewModelScope.launch {
            val song = getSongUseCase(songId)
            val copy = song.copy(
                id = song.id,
                title = song.title,
                lyrics = song.lyrics,
                chords = song.chords,
                defaultTonality = tonality,
                modulations = song.modulations,
                vocalistTonality = song.vocalistTonality,
                soloPart = song.soloPart,
                tempo = song.tempo
            )
            addSongUseCase(copy)
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