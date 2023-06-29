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

    private val _chords = MutableLiveData<String>()
    val chords: LiveData<String> get() = _chords

    private val _capo = MutableLiveData(0)
    private val _tonalityPosition = MutableLiveData<Int>()

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = getSongUseCase.invoke(songId)
            _song.postValue(song)
            _chords.postValue(convertNumbersToNotes(song.defaultTonality, song.chords))
            _tonalityPosition.postValue(getTonalityPosition(convertTonalityToSymbol(song.defaultTonality)))
        }
    }

    fun changeCapo(position: Int) {
        _capo.value = position
        transpose()
    }

    fun changeTonality(tonality: String) {
        _tonalityPosition.value = getTonalityPosition(tonality)
        transpose()
    }

    private fun transpose() {
        var newTonalityPosition = _tonalityPosition.value!! - _capo.value!!
        if (newTonalityPosition < 0) newTonalityPosition += 12
        _chords.value = _song.value?.chords?.let {
            convertNumbersToNotes(convertStringToTonality(getTonality(newTonalityPosition)), it)
        }
    }
}