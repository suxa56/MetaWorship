package uz.suxa.metaworship.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VocalistDao {

//    @Query("SELECT id, title, SUBSTR(lyrics, 1, 150) as lyrics, defaultTonality, vocalist, tonality FROM songs")
//    fun getSongs(): LiveData<List<SongDbModelDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVocalist(vocalistDbModel: VocalistDbModel)

//    @Query("DELETE FROM songs WHERE id=:songId")
//    fun deleteSong(songId: String)
//
//    @Query("SELECT * FROM songs WHERE id=:songId LIMIT 1")
//    fun getSong(songId: String): SongDbModel
}