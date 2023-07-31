package uz.suxa.metaworship.domain.usecase.song

import uz.suxa.metaworship.domain.repo.SongRepo

class GetSongListByQueryUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(query: String) = repo.getSongListByQuery(query)
}