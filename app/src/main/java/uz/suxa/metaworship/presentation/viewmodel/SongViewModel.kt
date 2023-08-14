package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SoloPart
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.domain.model.VocalistTonality
import uz.suxa.metaworship.domain.usecase.song.DeleteSongUseCase
import uz.suxa.metaworship.domain.usecase.song.GetSongUseCase

class SongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val getSongUseCase = GetSongUseCase(repo)
    private val deleteSongUseCase = DeleteSongUseCase(repo)

    private val _song = MutableLiveData<SongModel>()
    val song: LiveData<SongModel> get() = _song

    private val _chords = MutableLiveData<String>()
    val chords: LiveData<String> get() = _chords

    private val _capo = MutableLiveData(0)
    private val _tonalityPosition = MutableLiveData<Int>()

    private val _soloParts = MutableLiveData<List<SoloPart>>()
    val soloParts: LiveData<List<SoloPart>> get() = _soloParts

    private val _vocalistTonality = MutableLiveData<List<VocalistTonality>>()
    val vocalistTonality: LiveData<List<VocalistTonality>> get() = _vocalistTonality

    fun getSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val song = getSongUseCase.invoke(songId)
            _song.postValue(song)
            _chords.postValue(convertNumbersToNotes(song.defaultTonality, song.chords))
            _tonalityPosition.postValue(getTonalityPosition(convertTonalityToSymbol(song.defaultTonality)))
            _vocalistTonality.postValue(song.vocalistTonality)

            transposeSolo(song.soloPart, song.defaultTonality)
        }
    }

    fun changeCapo(position: Int) {
        _capo.value = position
        transpose()
    }

    fun fillTonalities(tonalities: Array<String>, vocalistTonality: List<VocalistTonality>): Array<String> {
        val map = hashMapOf<String, String>()
        vocalistTonality.forEach { item ->
            if (map[item.tonality].isNullOrBlank()) {
                map[item.tonality] = item.vocalist
            } else {
                map[item.tonality] += ", ${item.vocalist}"
            }
        }

        map.forEach { (tonality, vocalist) ->
            tonalities[tonalities.indexOf(tonality)] += " - $vocalist"
        }
        return tonalities
    }

    fun changeTonality(tonality: String) {
        _tonalityPosition.value = getTonalityPosition(tonality)
        transposeSolo(_song.value?.soloPart!!, convertStringToTonality(tonality))
        transpose()
    }

    fun deleteSong(songId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSongUseCase(songId)
        }
    }

    private fun transpose() {
        var newTonalityPosition = _tonalityPosition.value!! - _capo.value!!
        if (newTonalityPosition < 0) newTonalityPosition += 12
        _chords.value = _song.value?.chords?.let {
            convertNumbersToNotes(convertStringToTonality(getTonality(newTonalityPosition)), it)
        }
    }

    private fun transposeSolo(soloParts: List<SoloPart>, tonality: Tonality) {
        val convertedSoloParts = soloParts.map {
            SoloPart(it.part, convertNumbersToNotes(tonality, it.solo))
        }.toList()
        _soloParts.postValue(convertedSoloParts)
    }
}