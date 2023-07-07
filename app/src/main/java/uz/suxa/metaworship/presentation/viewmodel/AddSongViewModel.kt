package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.SongRepoImpl
import uz.suxa.metaworship.domain.model.SoloPart
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.VocalistTonality
import uz.suxa.metaworship.domain.usecase.AddSongUseCase
import java.util.UUID

class AddSongViewModel(application: Application) : TonalityViewModel(application) {

    private val repo = SongRepoImpl(application)
    private val addSongUseCase = AddSongUseCase(repo)


    private val _titleError = MutableLiveData<Boolean>()
    val titleError: LiveData<Boolean> get() = _titleError
    private val _tonalityError = MutableLiveData<Boolean>()
    val tonalityError: LiveData<Boolean> get() = _tonalityError
    private val _chordsError = MutableLiveData<Boolean>()
    val chordsError: LiveData<Boolean> get() = _chordsError
    private val _modulationError = MutableLiveData<List<Int>>()
    val modulationError: LiveData<List<Int>> get() = _modulationError
    private val _vocalistError = MutableLiveData<List<Int>>()
    val vocalistError: LiveData<List<Int>> get() = _vocalistError
    private val _vocalistTonalityError = MutableLiveData<List<Int>>()
    val vocalistTonalityError: LiveData<List<Int>> get() = _vocalistTonalityError
    private val _soloPartError = MutableLiveData<List<Int>>()
    val soloPartError: LiveData<List<Int>> get() = _soloPartError
    private val _soloError = MutableLiveData<List<Int>>()
    val soloError: LiveData<List<Int>> get() = _soloError

    fun addSong(
        title: String?,
        lyrics: String?,
        chords: String?,
        tonalityString: String?,
        modulations: MutableList<String>,
        vocalists: MutableList<String>,
        tonalities: MutableList<String>,
        soloParts: MutableList<String>,
        solos: MutableList<String>,
        tempo: String?,
        shouldClose: ShouldClose?
    ) {
        checkFields(title, tonalityString, chords, modulations, vocalists, tonalities, soloParts, solos)
        if (!_titleError.value!! &&
            !_tonalityError.value!! &&
            !_chordsError.value!! &&
            _modulationError.value!!.isEmpty() &&
            _vocalistError.value!!.isEmpty() &&
            _vocalistTonalityError.value!!.isEmpty() &&
            _soloPartError.value!!.isEmpty() &&
            _soloError.value!!.isEmpty()
        ) {
            val tonality = convertStringToTonality(tonalityString)
            val vocalistTonality = mutableListOf<VocalistTonality>()
            for ((index, _) in vocalists.withIndex()) {
                vocalistTonality.add(
                    VocalistTonality(
                        vocalists[index],
                        tonalities[index]
                    )
                )
            }

            val soloPartList = mutableListOf<SoloPart>()
            for ((index, _) in soloParts.withIndex()) {
                soloPartList.add(
                    SoloPart(
                        soloParts[index],
                        convertNotesToNumbers(tonality, solos[index])
                    )
                )
            }
            viewModelScope.launch {
                val song = SongModel(
                    id = UUID.randomUUID().toString(),
                    title = title ?: "",
                    lyrics = lyrics ?: "",
                    chords = convertNotesToNumbers(tonality, chords ?: ""),
                    defaultTonality = tonality,
                    modulations = convertModulationToString(tonality, modulations),
                    vocalistTonality = vocalistTonality,
                    soloPart = soloPartList,
                    tempo = getTempo(tempo)
                )
                addSongUseCase(song)
            }
            shouldClose?.onComplete()
        }
    }

    private fun checkFields(
        title: String?,
        tonalityString: String?,
        chords: String?,
        modulations: MutableList<String>,
        vocalists: MutableList<String>,
        tonalities: MutableList<String>,
        soloParts: MutableList<String>,
        solos: MutableList<String>
    ) {
        // Title Error
        _titleError.value = title.isNullOrBlank()

        // if tonality isn't empty then chords must filled and vice versa
        if (!tonalityString.isNullOrBlank() && chords.isNullOrBlank()) {
            _chordsError.value = true
            _tonalityError.value = false
        } else if (tonalityString.isNullOrBlank() && !chords.isNullOrBlank()) {
            _chordsError.value = false
            _tonalityError.value = true
        }

        // Remove tonality and chords error if both fields are empty
        if (
            !tonalityString.isNullOrBlank() && !chords.isNullOrBlank() ||
            tonalityString.isNullOrBlank() && chords.isNullOrBlank()
        ) {
            _chordsError.value = false
            _tonalityError.value = false
        }

        // If vocalist or his(her) tonality added -> default tonality must be defined
        if ((vocalists.isNotEmpty() || tonalities.isNotEmpty()) && tonalityString.isNullOrBlank()) {
            _tonalityError.value = true
        }

        var index: Int
        // Modulation blank error
        val blankModulation = mutableListOf<Int>()
        while (modulations.contains(BLANK_SYMBOL)) {
            index = modulations.indexOf(BLANK_SYMBOL)
            blankModulation.add(index)
            modulations.remove(BLANK_SYMBOL)
            modulations.add(index, REPLACED_SYMBOL)
        }
        _modulationError.value = blankModulation

        // Vocalist blank error
        val blankVocalists = mutableListOf<Int>()
        while (vocalists.contains(BLANK_SYMBOL)) {
            index = vocalists.indexOf(BLANK_SYMBOL)
            blankVocalists.add(index)
            vocalists.remove(BLANK_SYMBOL)
            vocalists.add(index, REPLACED_SYMBOL)
        }
        _vocalistError.value = blankVocalists

        // VocalistTonality blank error
        val blankVocalistsTonality = mutableListOf<Int>()
        while (tonalities.contains(BLANK_SYMBOL)) {
            index = tonalities.indexOf(BLANK_SYMBOL)
            blankVocalistsTonality.add(index)
            tonalities.remove(BLANK_SYMBOL)
            tonalities.add(index, REPLACED_SYMBOL)
        }
        _vocalistTonalityError.value = blankVocalistsTonality

        // VocalistTonality blank error
        val blankSoloParts = mutableListOf<Int>()
        while (soloParts.contains(BLANK_SYMBOL)) {
            index = soloParts.indexOf(BLANK_SYMBOL)
            blankSoloParts.add(index)
            soloParts.remove(BLANK_SYMBOL)
            soloParts.add(index, REPLACED_SYMBOL)
        }
        _soloPartError.value = blankSoloParts

        // VocalistTonality blank error
        val blankSolos = mutableListOf<Int>()
        while (solos.contains(BLANK_SYMBOL)) {
            index = solos.indexOf(BLANK_SYMBOL)
            blankSolos.add(index)
            solos.remove(BLANK_SYMBOL)
            solos.add(index, REPLACED_SYMBOL)
        }
        _soloError.value = blankSolos
    }

    private fun getTempo(tempo: String?): Int {
        return if (tempo.isNullOrBlank()) {
            UNDEFINED_TEMPO
        } else {
            tempo.toInt()
        }
    }

    interface ShouldClose {
        fun onComplete()
    }

    companion object {
        private const val UNDEFINED_TEMPO = -1
        private const val BLANK_SYMBOL = ""
        private const val REPLACED_SYMBOL = "replace"
    }
}