package uz.suxa.metaworship.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocalists")
data class VocalistDbModel(
    @PrimaryKey
    val id: String,
    val name: String
)