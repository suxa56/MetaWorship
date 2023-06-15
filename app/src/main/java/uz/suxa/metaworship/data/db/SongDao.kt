package uz.suxa.metaworship.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongDao {

    @Query("SELECT * FROM songs")
    fun getSongs(): LiveData<List<SongDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(songDBModel: SongDbModel)

    @Query("DELETE FROM songs WHERE id=:songId")
    fun deleteSong(songId: String)

    @Query("SELECT * FROM songs WHERE id=:songId LIMIT 1")
    fun getSong(songId: Int): LiveData<SongDbModel>
}