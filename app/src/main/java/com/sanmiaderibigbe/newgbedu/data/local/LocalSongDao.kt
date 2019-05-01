package com.sanmiaderibigbe.newgbedu.data.local

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong

@Dao
interface LocalSongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSongs(localSongs: List<LocalSong>)

    @Query("SELECT * FROM  song_list_table ORDER BY date DESC")
    fun loadAllSongs(): LiveData<List<LocalSong>>

    //Todo add delete methode. To delete old song. where song older than certain time.
    @Query("SELECT * FROM song_list_table WHERE releaseDate = :currentDate ORDER BY songName  ")
    fun loadSongReleasedOnCurrentDate(currentDate: String)  : List<LocalSong>

}