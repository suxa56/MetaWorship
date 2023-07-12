package uz.suxa.metaworship.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VocalistDao {

    @Query("SELECT id, name FROM vocalists")
    fun getVocalists(): LiveData<List<VocalistDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVocalist(vocalistDbModel: VocalistDbModel)

//    @Query("DELETE FROM songs WHERE id=:songId")
//    fun deleteSong(songId: String)
//
//    @Query("SELECT * FROM songs WHERE id=:songId LIMIT 1")
//    fun getSong(songId: String): SongDbModel
}