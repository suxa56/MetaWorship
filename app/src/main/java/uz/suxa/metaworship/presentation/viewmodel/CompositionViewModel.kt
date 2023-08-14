package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suxa.metaworship.data.CompositionRepoImpl
import uz.suxa.metaworship.domain.model.CompositionModel
import uz.suxa.metaworship.domain.usecase.composition.AddCompositionUseCase
import java.util.UUID

class CompositionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = CompositionRepoImpl(application)
    private val addCompositionUseCase = AddCompositionUseCase(repo)

    fun addComposition(label: String, songId: String, tonality: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val composition = CompositionModel(
                id = "comp_" + UUID.randomUUID().toString(),
                name = label,
                songId = listOf(songId),
                tonality = listOf(tonality),
                creationDate = System.currentTimeMillis()
            )
            addCompositionUseCase(composition)
        }
    }
}