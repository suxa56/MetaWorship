package uz.suxa.metaworship.domain.usecase

import uz.suxa.metaworship.domain.repo.VocalistRepo

class GetVocalistWithSongCountUseCase(
    private val repo: VocalistRepo
) {
    suspend operator fun invoke() = repo.getVocalistWithSongCount()
}