package uz.suxa.metaworship.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.repo.VocalistRepo

class VocalistRepoImpl(
    application: Application
): VocalistRepo {

    private val vocalistDao = AppDatabase.getInstance(application).vocalistDao()
    private val mapper = VocalistMapper()

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
        vocalistDao.addVocalist(mapper.mapEntityToDbModel(vocalist))
    }

    override suspend fun deleteVocalist(vocalistId: String) {
        TODO("Not yet implemented")
    }

}