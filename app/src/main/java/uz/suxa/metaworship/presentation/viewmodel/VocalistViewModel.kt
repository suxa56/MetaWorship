package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.usecase.AddVocalistUseCase
import uz.suxa.metaworship.domain.usecase.GetVocalistWithSongCountUseCase
import java.util.UUID

class VocalistViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = VocalistRepoImpl(application)
    private val addVocalistUseCase = AddVocalistUseCase(repo)
    private val getVocalistWithSongCountUseCase = GetVocalistWithSongCountUseCase(repo)

    private val _vocalistsDto = MediatorLiveData<List<VocalistSongDto>>()
    val vocalistsDto: LiveData<List<VocalistSongDto>> get() = _vocalistsDto

    fun getVocalists() {
        viewModelScope.launch {
            _vocalistsDto.addSource(getVocalistWithSongCountUseCase()) {
                _vocalistsDto.value = it
            }
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
}