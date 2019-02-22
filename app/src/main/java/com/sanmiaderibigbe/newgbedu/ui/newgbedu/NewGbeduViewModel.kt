package com.sanmiaderibigbe.newgbedu.ui.newgbedu

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong
import com.sanmiaderibigbe.newgbedu.data.Repository
import com.sanmiaderibigbe.newgbedu.data.remote.NetWorkState

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
}