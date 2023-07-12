package uz.suxa.metaworship.domain.usecase

import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.repo.VocalistRepo

class AddVocalistUseCase(
    private val repo: VocalistRepo
) {
    suspend operator fun invoke(vocalist: VocalistModel) = repo.addVocalist(vocalist)
}