package com.sanmiaderibigbe.newgbedu.data.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class NewSongList(@SerializedName("newSong") @Expose  val newSong: List<Song>? = null)
