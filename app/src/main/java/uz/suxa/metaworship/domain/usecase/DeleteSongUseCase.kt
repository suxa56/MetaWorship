package uz.suxa.metaworship.domain.usecase

import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class DeleteSongUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(song: SongModel) = repo.deleteSong(song)
}