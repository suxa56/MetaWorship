package uz.suxa.metaworship.domain.repo

import uz.suxa.metaworship.domain.model.SongModel

interface SongRepo {

    suspend fun addSong(song: SongModel)
}