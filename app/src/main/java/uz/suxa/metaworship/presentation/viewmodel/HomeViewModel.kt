package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.usecase.song.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByQueryUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListByVocalistUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongListUseCase
import uz.suxa.metaworship.domain.usecase.vocalist.GetVocalistWithSongCountUseCase

class HomeViewModel(application: Application) : TonalityViewModel(application) {

    private val songRepo = SongRepoImpl(application)
    private val getSongList = GetSongListUseCase(songRepo)
    private val getSongListByVocalist = GetSongListByVocalistUseCase(songRepo)
    private val getSongListByQuery = GetSongListByQueryUseCase(songRepo)
    private val deleteSongUseCase = DeleteSongUseCase(songRepo)

    private val vocalistRepo = VocalistRepoImpl(application)
    private val getVocalistWithSongCountUseCase = GetVocalistWithSongCountUseCase(vocalistRepo)


    private val _songs = MediatorLiveData<List<SongModel>>()
    val songs: LiveData<List<SongModel>> get() = _songs

    private val _vocalistsDto = MediatorLiveData<List<VocalistSongDto>>()
    val vocalistsDto: LiveData<List<VocalistSongDto>> get() = _vocalistsDto

    private var activeSource: LiveData<List<SongModel>>? = null

    init {
        viewModelScope.launch {
            _songs.addSource(getSongList()) {
                _songs.value = it
            }
        }
    }

    fun copySongChords(song: SongModel, tonality: Tonality): CharSequence {
        var text = song.title + ": " + convertTonalityToSymbol(tonality) + "\n"
        text += convertNumbersToNotes(tonality, song.chords)
        return text
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