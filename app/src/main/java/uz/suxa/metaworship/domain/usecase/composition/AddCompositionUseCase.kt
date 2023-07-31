package uz.suxa.metaworship.domain.usecase.composition

import uz.suxa.metaworship.domain.model.CompositionModel
import uz.suxa.metaworship.domain.repo.CompositionRepo

class AddCompositionUseCase(
    private val repo: CompositionRepo
) {
    suspend operator fun invoke(composition: CompositionModel) = repo.addComposition(composition)
}