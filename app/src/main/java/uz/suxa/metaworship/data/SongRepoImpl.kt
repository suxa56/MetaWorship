package uz.suxa.metaworship.data

import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class SongRepoImpl: SongRepo {

    override suspend fun addSong(song: SongModel) {
        println(song.toString())
    }
}