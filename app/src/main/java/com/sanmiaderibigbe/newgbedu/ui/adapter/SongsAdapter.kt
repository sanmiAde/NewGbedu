package com.sanmiaderibigbe.newgbedu.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong
import com.sanmiaderibigbe.newgbedu.databinding.SongListItemBinding


class SongsAdapter(context: Context, val openSongWebView: OpenSongWebView, val openArtistWebView: OpenArtistWebView) : RecyclerView.Adapter<SongsAdapter.ViewHolder>() {


    private var songList: List<LocalSong>? = null

    interface OpenSongWebView{
        fun onClickSongTitle(song: LocalSong)

    }

    interface OpenArtistWebView{
        fun onClickArtist(song: LocalSong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = SongListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    fun setTodoList(songs: List<LocalSong>?) {
        songList = songs
        notifyDataSetChanged()
    }

    fun clear() {
        songList = null
        notifyDataSetChanged()
    }



    override fun getItemCount(): Int =  songList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        val song: LocalSong? = songList?.get(p1)
        holder.newSongsBinding.song = song
    }

    inner class ViewHolder(val newSongsBinding: SongListItemBinding) : RecyclerView.ViewHolder(newSongsBinding.root), View.OnClickListener{

        init {
            newSongsBinding.songTitleTxt.setOnClickListener(this)
            newSongsBinding.artistNameTxt.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val selectedViewID = v?.id
            val song = songList?.get(adapterPosition)
            when(selectedViewID) {
                newSongsBinding.songTitleTxt.id -> {
                    openSongWebView.onClickSongTitle(song!!)
                }
                newSongsBinding.artistNameTxt.id -> {
                    openArtistWebView.onClickArtist(song!!)
                }
            }


        }

    }

}