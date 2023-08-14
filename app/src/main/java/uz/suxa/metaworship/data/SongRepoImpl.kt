package uz.suxa.metaworship.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uz.suxa.metaworship.data.db.AppDatabase
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.repo.SongRepo

class SongRepoImpl(
    application: Application
): SongRepo {

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

    override suspend fun uploadSongs() {
        MediatorLiveData<List<SongModel>>().apply {
            addSource(songDao.getFullSongs()) {list ->
                list.forEach { song ->
                    database.child(song.id).setValue(song)
                }
            }
        }
    }

    override suspend fun downloadSongs() {
        database.get().addOnSuccessListener { songs ->
            songs.children.forEach {
                Log.d("download-key", it.key.toString())
                Log.d("download-value", it.value.toString())
                Log.d("download-childrenCount", it.childrenCount.toString())
            }
        }
    }
}