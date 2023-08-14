package uz.suxa.metaworship.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface CompositionDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addComposition(compositionDbModel: CompositionDbModel)
}