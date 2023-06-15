package uz.suxa.metaworship.domain.repo

import androidx.lifecycle.LiveData
import uz.suxa.metaworship.domain.model.SongModel

interface SongRepo {

    suspend fun getSongList(): LiveData<List<SongModel>>
    suspend fun addSong(song: SongModel)

    suspend fun deleteSong(songId: String)
}