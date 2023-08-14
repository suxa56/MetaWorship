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

    @Query("SELECT v.name, count(s.id) AS songsCount FROM songs s, vocalists v WHERE s.vocalist LIKE '%'||v.name||'%' GROUP BY v.name ORDER BY v.name")
    fun getVocalistsWithSongCount(): LiveData<List<VocalistSongDbDto>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addVocalist(vocalistDbModel: VocalistDbModel)

//    @Query("DELETE FROM songs WHERE id=:songId")
//    fun deleteSong(songId: String)
//
//    @Query("SELECT * FROM songs WHERE id=:songId LIMIT 1")
//    fun getSong(songId: String): SongDbModel
}