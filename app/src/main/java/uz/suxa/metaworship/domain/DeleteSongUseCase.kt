package uz.suxa.metaworship.domain

class DeleteSongUseCase(
    private val repo: SongsRepo
) {
    operator fun invoke() = repo.deleteSong()
}