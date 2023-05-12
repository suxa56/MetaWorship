package uz.suxa.metaworship.domain

class EditSongUseCase(
    private val repo: SongsRepo
) {
    operator fun invoke() = repo.editSong()
}