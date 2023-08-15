package uz.suxa.metaworship.domain.usecase.vocalist

import uz.suxa.metaworship.domain.repo.VocalistRepo

class DeleteVocalistUseCase(
    private val repo: VocalistRepo
) {
    suspend operator fun invoke(vocalistId: String) = repo.deleteVocalist(vocalistId)
}