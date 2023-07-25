package uz.suxa.metaworship.domain.repo

import androidx.lifecycle.LiveData
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.VocalistModel

interface VocalistRepo {

    suspend fun getVocalistList(): LiveData<List<VocalistModel>>

    suspend fun getVocalistWithSongCount(): LiveData<List<VocalistSongDto>>

    suspend fun getVocalist(vocalistId: String): VocalistModel

    suspend fun addVocalist(vocalist: VocalistModel)

    suspend fun deleteVocalist(vocalistId: String)
}