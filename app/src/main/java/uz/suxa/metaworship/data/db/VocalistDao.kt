package uz.suxa.metaworship.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VocalistDao {

    @Query("SELECT * FROM vocalists")
    fun getVocalists(): LiveData<List<VocalistDbModel>>

    @Query("SELECT * FROM vocalists")
    suspend fun getVocalistsList(): List<VocalistDbModel>

    @Query("SELECT v.name, count(s.id) AS songsCount FROM songs s, vocalists v WHERE s.vocalist LIKE '%'||v.name||'%' GROUP BY v.name ORDER BY v.name")
    fun getVocalistsWithSongCount(): LiveData<List<VocalistSongDbDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addVocalist(vocalistDbModel: VocalistDbModel)

    @Query("DELETE FROM vocalists WHERE id=:vocalistId")
    suspend fun deleteVocalist(vocalistId: String)
}