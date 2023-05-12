package uz.suxa.metaworship.domain

import androidx.lifecycle.LiveData
import uz.suxa.metaworship.domain.model.Song

interface SongsRepo {

    fun getAllSongs(): LiveData<List<Song>>

    fun createSong()

    fun editSong()

    fun deleteSong()

    fun getSong(id: String): LiveData<Song>

    suspend fun loadSongs()

}