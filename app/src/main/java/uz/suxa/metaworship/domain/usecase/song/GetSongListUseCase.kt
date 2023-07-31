package uz.suxa.metaworship.domain.usecase.song

import uz.suxa.metaworship.domain.repo.SongRepo

class GetSongListUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke() = repo.getSongList()
}