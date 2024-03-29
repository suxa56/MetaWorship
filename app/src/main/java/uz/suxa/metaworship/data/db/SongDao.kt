package uz.suxa.metaworship.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongDao {

    @Query(
        "SELECT id, title, SUBSTR(lyrics, 1, 100) as lyrics, " +
                "defaultTonality, vocalist, tonality " +
                "FROM songs"
    )
    fun getSongs(): LiveData<List<SongDbModelDto>>

    @Query("SELECT * FROM songs")
    suspend fun getFullSongs(): List<SongDbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(songDBModel: SongDbModel)

    @Query("DELETE FROM songs WHERE id=:songId")
    suspend fun deleteSong(songId: String)

    @Query("SELECT * FROM songs WHERE id=:songId LIMIT 1")
    suspend fun getSong(songId: String): SongDbModel

    @Query(
        "SELECT id, title, SUBSTR(lyrics, 1, 100) as lyrics, " +
                "defaultTonality, vocalist, tonality " +
                "FROM songs " +
                "WHERE vocalist LIKE '%'||:vocalist||'%'"
    )
    fun getSongsByVocalist(vocalist: String): LiveData<List<SongDbModelDto>>

    @Query(
        "SELECT id, title, SUBSTR(lyrics, 1, 100) as lyrics, " +
                "defaultTonality, vocalist, tonality " +
                "FROM songs " +
                "WHERE title LIKE '%'||:query||'%' OR lyrics LIKE '%'||:query||'%' "
    )
    fun getSongsByQuery(query: String): LiveData<List<SongDbModelDto>>

    @Query("SELECT lyrics FROM songs WHERE id=:songId LIMIT 1")
    suspend fun getLyrics(songId: String): String

    @Query("SELECT chords FROM songs WHERE id=:songId LIMIT 1")
    suspend fun getChords(songId: String): String

    @Query("SELECT MAX(lastUpdate) FROM songs")
    suspend fun getLastUpdate(): Long
}