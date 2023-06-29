package uz.suxa.metaworship.domain.usecase

import uz.suxa.metaworship.domain.repo.SongRepo

class GetSongUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(songId: String) = repo.getSong(songId)
}