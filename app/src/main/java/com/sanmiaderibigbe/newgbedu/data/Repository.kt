package com.sanmiaderibigbe.newgbedu.data

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong
import com.sanmiaderibigbe.newgbedu.data.local.LocalSongDao
import com.sanmiaderibigbe.newgbedu.data.remote.NetWorkState
import com.sanmiaderibigbe.newgbedu.data.remote.NewSongList
import com.sanmiaderibigbe.newgbedu.data.remote.RetrofitInstance
import com.sanmiaderibigbe.newgbedu.data.remote.Song
import com.sanmiaderibigbe.newgbedu.utils.convertDateStringToDateObject
import com.sanmiaderibigbe.newgbedu.utils.convertDateToString
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Repository(private val application: Application) {

    private val networkState: MutableLiveData<NetWorkState> = MutableLiveData()
    private val doa: LocalSongDao

    init {
        val db = AppDatabase.getDatabase(application, false)
        doa = db.localSongDao()
    }

    fun initApiCall(): LiveData<List<LocalSong>> {
        networkState.value = NetWorkState.NotLoaded

        val service = RetrofitInstance.initRetrofitInstance()
        val call: Call<NewSongList> = service.getNewSongs()

        performNetworkCall(call)

        return getLocalCache()
    }

    fun getLocalCache(): LiveData<List<LocalSong>> {
        return doa.loadAllSongs()
    }

    fun getSongsReleasedToday(): List<LocalSong> = doa.loadSongReleasedOnCurrentDate(convertDateToString(Date()))


    private fun performNetworkCall(call: Call<NewSongList>) {
        networkState.value = NetWorkState.Loading

        call.enqueue(object : Callback<NewSongList> {
            override fun onFailure(call: Call<NewSongList>, t: Throwable) {
                Log.e(TAG, t.message)
                networkState.value = NetWorkState.Error(t.message)

            }

            override fun onResponse(call: Call<NewSongList>, response: Response<NewSongList>) {
                when {
                    response.isSuccessful -> {
                        networkState.value = NetWorkState.Success

                        doAsync {
                            doa.insertSongs(convertToLocalSongDTO(response.body()?.newSong))
                        }
                    }
                }
            }
        })
    }

    private fun convertToLocalSongDTO(newRemoteSongs: List<Song>?): List<LocalSong> {
        val localSongDtos = mutableListOf<LocalSong>()

        newRemoteSongs?.forEach {

            localSongDtos.add(
                LocalSong(
                    it.songName!!,
                    it.artistName!!,
                    it.pictureURL,
                    it.releaseDate,
                    convertDateStringToDateObject(it.releaseDate!!)
                )
            )

        }

        Log.d(TAG, localSongDtos.size.toString())
        return localSongDtos

    }


    fun getNetworkState(): LiveData<NetWorkState> {
        return networkState
    }


    companion object {
        private const val TAG = "Respository"

        private var instance: Repository? = null

        @Synchronized
        fun getRepository(application: Application): Repository {
            if (instance == null) {
                instance = Repository(application)
            }
            return instance!!
        }
    }


}