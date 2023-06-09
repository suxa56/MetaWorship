package uz.suxa.metaworship.domain

import androidx.lifecycle.LiveData
import uz.suxa.metaworship.domain.model.SongModel

interface SongsRepo {

    fun getAllSongs(): LiveData<List<SongModel>>

    fun createSong()

    fun editSong()

    fun deleteSong()

    fun getSong(id: String): LiveData<SongModel>

    suspend fun loadSongs()

}