package uz.suxa.metaworship.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.data.db.VocalistDbModel
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.repo.VocalistRepo

class VocalistRepoImpl(
    application: Application
) : VocalistRepo {

    private val vocalistDao = AppDatabase.getInstance(application).vocalistDao()
    private val mapper = VocalistMapper()

    private val database = Firebase.database.getReference("vocalist")

    override suspend fun getVocalistList(): LiveData<List<VocalistModel>> {
        return MediatorLiveData<List<VocalistModel>>().apply {
            addSource(vocalistDao.getVocalists()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
    }

    override suspend fun getVocalistWithSongCount(): LiveData<List<VocalistSongDto>> {
        return MediatorLiveData<List<VocalistSongDto>>().apply {
            addSource(vocalistDao.getVocalistsWithSongCount()) {
                value = mapper.mapListDbDtoToListEntityDto(it)
            }
        }
    }

    override suspend fun getVocalist(vocalistId: String): VocalistModel {
        TODO("Not yet implemented")
    }

    override suspend fun addVocalist(vocalist: VocalistModel) {
        val vocalistDbModel = mapper.mapEntityToDbModel(vocalist)
        vocalistDao.addVocalist(vocalistDbModel)
        database.child(vocalist.id).setValue(vocalistDbModel)
    }

    override suspend fun deleteVocalist(vocalistId: String) {
        vocalistDao.deleteVocalist(vocalistId)
        database.child(vocalistId).removeValue()
    }

    override suspend fun sync() {
        val vocalistList = mutableListOf<VocalistDbModel>()
        val dataSnapshot = database.get().await()
        for (vocalist in dataSnapshot.children) {
            val vocalistMap = vocalist.getValue<Map<String, Any?>>()
            vocalistMap?.let {
                vocalistList.add(mapper.mapFirebaseToDbModel(it))
            }
        }
        withContext(Dispatchers.IO) {
            vocalistList.forEach {
                vocalistDao.addVocalist(it)
            }
        }

        vocalistDao.getVocalistsList().forEach {
            database.child(it.id).setValue(it)
        }

    }

}