package uz.suxa.metaworship.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class SongRepoImpl(
    application: Application
): SongRepo {

    private val songDao = AppDatabase.getInstance(application).songDao()
    private val mapper = SongMapper()

    override suspend fun getSongList(): LiveData<List<SongModel>> {
        return MediatorLiveData<List<SongModel>>().apply {
            addSource(songDao.getSongs()) {
                value = mapper.mapListDbModelToListEntity(it)
            }
        }
    }

    override suspend fun addSong(song: SongModel) {
        songDao.addSong(mapper.mapEntityToDbModel(song))
    }

    override suspend fun deleteSong(song: SongModel) {
        songDao.deleteSong(mapper.mapEntityToDbModel(song).id)
    }
}