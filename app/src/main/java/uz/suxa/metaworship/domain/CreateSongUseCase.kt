package uz.suxa.metaworship.domain

class CreateSongUseCase(
    private val repo: SongsRepo
) {
    operator fun invoke() = repo.createSong()
}