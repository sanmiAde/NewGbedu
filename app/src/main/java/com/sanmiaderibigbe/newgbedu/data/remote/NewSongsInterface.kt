package com.sanmiaderibigbe.newgbedu.data.remote

import com.sanmiaderibigbe.newgbedu.data.remote.NewSongList
import retrofit2.Call
import retrofit2.http.GET

interface NewSongsInterface {
    @GET("/")
    fun getNewSongs(): Call<NewSongList>
}