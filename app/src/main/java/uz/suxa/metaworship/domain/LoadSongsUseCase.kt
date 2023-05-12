package uz.suxa.metaworship.domain

class LoadSongsUseCase(
    private val repo: SongsRepo
) {
    suspend operator fun invoke() = repo.loadSongs()
}