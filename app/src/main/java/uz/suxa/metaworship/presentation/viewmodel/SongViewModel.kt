package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.usecase.GetSongUseCase

class SongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongUseCase = GetSongUseCase(repo)

    private val _song = MutableLiveData<SongModel>()
    val song: LiveData<SongModel> get() = _song

    private val _chords = MutableLiveData<String>()
    val chords: LiveData<String> get() = _chords

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = getSongUseCase.invoke(songId)
            _song.postValue(song)
            _chords.postValue(convertNumbersToNotes(song.defaultTonality, song.chords))
        }
    }

    fun transpose(tonality: String) {
        val castedTonality = Tonality.valueOf(
            tonality.replace("b", "_FLAT").replace("#", "_SHARP")
        )
        _chords.value = _song.value?.chords?.let { convertNumbersToNotes(castedTonality, it) }
    }

    fun parseTonality(tonality: Tonality): String {
        return tonality.toString()
            .replace("_SHARP", "#")
            .replace("_FLAT", "b")
    }
}