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
import uz.suxa.metaworship.data.db.SongDbModel
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class SongRepoImpl(
    application: Application
) : SongRepo {

    private val songDao = AppDatabase.getInstance(application).songDao()
    private val mapper = SongMapper()

    private val database = Firebase.database.getReference("song")

    override suspend fun getSongList(): LiveData<List<SongModel>> {
        return MediatorLiveData<List<SongModel>>().apply {
            addSource(songDao.getSongs()) {
                value = mapper.mapListDbModelDtoToListEntity(it)
            }
        }
    }

    override suspend fun getSong(songId: String): SongModel {
        return mapper.mapDbModelToEntity(songDao.getSong(songId))
    }

    override suspend fun getSongListByVocalist(vocalist: String): LiveData<List<SongModel>> {
        return MediatorLiveData<List<SongModel>>().apply {
            addSource(songDao.getSongsByVocalist(vocalist)) {
                value = mapper.mapListDbModelDtoToListEntity(it)
            }
        }
    }

    override suspend fun getSongListByQuery(query: String): LiveData<List<SongModel>> {
        return MediatorLiveData<List<SongModel>>().apply {
            addSource(songDao.getSongsByQuery(query)) {
                value = mapper.mapListDbModelDtoToListEntity(it)
            }
        }
    }

    override suspend fun addSong(song: SongModel) {
        val songDBModel = mapper.mapEntityToDbModel(song)
        songDao.addSong(songDBModel)
        database.child(songDBModel.id).setValue(songDBModel)
    }

    override suspend fun deleteSong(songId: String) {
        songDao.deleteSong(songId)
        database.child(songId).removeValue()
    }

    override suspend fun getLyrics(songId: String): String {
        return songDao.getLyrics(songId)
    }

    override suspend fun getChords(songId: String): String {
        return songDao.getChords(songId)
    }

    override suspend fun sync() {
        val songList = mutableListOf<SongDbModel>()
        val songs = database.get().await()
        songs.children.forEach { song ->
            val songMap = song.getValue<Map<String, Any?>>()
            songMap?.let {
                songList.add(mapper.mapFirebaseToDbModel(it))
            }
        }
        withContext(Dispatchers.IO) {
            songList.forEach {
                songDao.addSong(it)
            }
        }

        songDao.getFullSongs().forEach {
            database.child(it.id).setValue(it)
        }
    }
}