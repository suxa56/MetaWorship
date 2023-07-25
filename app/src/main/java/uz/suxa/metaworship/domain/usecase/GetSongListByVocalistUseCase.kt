package uz.suxa.metaworship.domain.usecase

import uz.suxa.metaworship.domain.repo.SongRepo

class GetSongListByVocalistUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(vocalist: String) = repo.getSongListByVocalist(vocalist)
}