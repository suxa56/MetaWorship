package uz.suxa.metaworship.domain

class GetSongUseCase(
    private val repo: SongsRepo
) {
    operator fun invoke(id: String) = repo.getSong(id)
}