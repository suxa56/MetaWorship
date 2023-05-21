package uz.suxa.metaworship.data

import android.app.Application
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class SongRepoImpl(
    application: Application
): SongRepo {

    private val songDao = AppDatabase.getInstance(application).songDao()
    private val mapper = SongMapper()

    override suspend fun addSong(song: SongModel) {
        songDao.addSong(mapper.mapEntityToDbModel(song))
        println(song.toString())
    }
}