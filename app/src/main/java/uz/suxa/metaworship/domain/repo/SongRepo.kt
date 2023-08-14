package uz.suxa.metaworship.domain.repo

import androidx.lifecycle.LiveData
import uz.suxa.metaworship.domain.model.SongModel

interface SongRepo {

    suspend fun getSongList(): LiveData<List<SongModel>>

    suspend fun getSong(songId: String): SongModel

    suspend fun getSongListByVocalist(vocalist: String): LiveData<List<SongModel>>

    suspend fun getSongListByQuery(query: String): LiveData<List<SongModel>>

    suspend fun addSong(song: SongModel)

    suspend fun deleteSong(songId: String)

    suspend fun getLyrics(songId: String): String

    suspend fun getChords(songId: String): String

    suspend fun uploadSongs()

    suspend fun downloadSongs()
}