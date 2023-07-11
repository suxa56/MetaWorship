package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.VocalistRepoImpl
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.usecase.AddVocalistUseCase
import java.util.UUID

class VocalistViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = VocalistRepoImpl(application)
    private val addVocalistUseCase = AddVocalistUseCase(repo)

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