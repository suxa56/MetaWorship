package uz.suxa.metaworship.domain.usecase.song

import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class AddSongUseCase(
    private val repo: SongRepo
) {
    suspend operator fun invoke(song: SongModel) = repo.addSong(song)
}