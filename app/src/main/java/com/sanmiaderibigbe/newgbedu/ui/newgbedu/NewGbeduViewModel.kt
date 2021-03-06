package com.sanmiaderibigbe.newgbedu.ui.newgbedu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong
import com.sanmiaderibigbe.newgbedu.data.Repository
import com.sanmiaderibigbe.newgbedu.data.remote.NetWorkState
import java.text.SimpleDateFormat
import java.util.*

class NewGbeduViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository = Repository.getRepository(application)

    fun getNewSongsOnline() : LiveData<List<LocalSong>> {
        return  repository.initApiCall()
    }

    fun getNetworkstate(): LiveData<NetWorkState> {
        return  repository.getNetworkState()
    }

    fun getCache(): LiveData<List<LocalSong>> {
       return repository.getLocalCache()
    }

    /***
     * @return Songs being released on current day.
     */
    fun getSongsCurrentlyReleasedToday() : LiveData<List<LocalSong>> {
       return  Transformations.map(repository.getLocalCache()) { localSong -> localSong.filter { it.releaseDate?.toLowerCase() == getCurrentDate() }}
    }

    private fun getCurrentDate() = convertDateToString(Date()).toLowerCase()

    private fun convertDateToString(date: Date): String {
        val dateFormat = SimpleDateFormat("d MMMM yyyy")
        return dateFormat.format(date)
    }

}