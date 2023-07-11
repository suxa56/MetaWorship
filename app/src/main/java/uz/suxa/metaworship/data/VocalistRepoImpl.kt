package uz.suxa.metaworship.data

import android.app.Application
import androidx.lifecycle.LiveData
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.domain.model.VocalistModel
import uz.suxa.metaworship.domain.repo.VocalistRepo

class VocalistRepoImpl(
    application: Application
): VocalistRepo {

    private val vocalistDao = AppDatabase.getInstance(application).vocalistDao()
    private val mapper = VocalistMapper()

    override suspend fun getVocalistList(): LiveData<List<VocalistModel>> {
        TODO("Not yet implemented")
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