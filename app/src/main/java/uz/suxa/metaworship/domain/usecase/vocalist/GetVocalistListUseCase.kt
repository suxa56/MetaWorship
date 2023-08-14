package uz.suxa.metaworship.domain.usecase.vocalist

import uz.suxa.metaworship.domain.repo.VocalistRepo

class GetVocalistListUseCase(
    private val repo: VocalistRepo
) {
    suspend operator fun invoke() = repo.getVocalistList()
}