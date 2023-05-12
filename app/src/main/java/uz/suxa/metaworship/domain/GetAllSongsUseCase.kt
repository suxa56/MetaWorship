package uz.suxa.metaworship.domain

class GetAllSongsUseCase(
    private val repo: SongsRepo
) {
    operator fun invoke() = repo.getAllSongs()
}