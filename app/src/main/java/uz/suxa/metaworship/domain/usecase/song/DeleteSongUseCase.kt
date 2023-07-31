package uz.suxa.metaworship.domain.usecase.song

import uz.suxa.metaworship.domain.repo.SongRepo

class DeleteSongUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(songId: String) = repo.deleteSong(songId)
}