package uz.suxa.metaworship.domain.usecase.vocalist

import uz.suxa.metaworship.domain.repo.VocalistRepo

class SyncVocalistsUseCase(
    private val repo: VocalistRepo
) {
    suspend operator fun invoke() = repo.sync()
}