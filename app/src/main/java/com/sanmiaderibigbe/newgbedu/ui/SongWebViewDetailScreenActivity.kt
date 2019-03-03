package com.sanmiaderibigbe.newgbedu.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.sanmiaderibigbe.newgbedu.R
import kotlinx.android.synthetic.main.activity_song_web_view_detail_screen.*


class SongWebViewDetailScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_web_view_detail_screen)
        val songTitle : String =  intent.getStringExtra(SONG_TITLE_EXTRA)
        val songArtist : String = intent.getStringExtra(SONG_ARTIST_EXTRA)
        val shouldLoadSongPage : Boolean = intent.getBooleanExtra(SHOULD_LOAD_SONG_PAGE_INSTEAD_OF_ARTIST, true)

        val songSiteUrl = buildUrl(songTitle, songArtist, shouldLoadSongPage)
        Log.d("song", songSiteUrl)
        val webSettings = song_web_view.settings
        song_web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d("song",request.url.toString())
                    view.loadUrl(request.url.toString())
                }else{

                }
                return false
            }
        }
        webSettings.javaScriptEnabled = true
        webSettings.allowUniversalAccessFromFileURLs
        webSettings.allowContentAccess = true
        webSettings.domStorageEnabled = true

        song_web_view.loadUrl(songSiteUrl)
    }


    private fun buildUrl(songTitle: String, songArtist: String, shouldLoadSongPage: Boolean): String {
       val builder =  Uri.Builder()

       return when(shouldLoadSongPage){
            true -> {
                builder.scheme("https")
                    .authority(LAST_FM_BASE_URL)
                    .appendPath("music")
                    .appendPath(songArtist)
                    .appendPath(songTitle).build().toString()
            }
           false -> {
               builder.scheme("https")
                   .authority(LAST_FM_BASE_URL)
                   .appendPath("music")
                   .appendPath(songArtist)
                  .build().toString()
           }
        }

    }


companion object {
    private const val LAST_FM_BASE_URL = "www.last.fm"
   private const val SONG_TITLE_EXTRA = "com.sanmiaderibigbe.newgbedu.songTitleExtra"
    private const val SONG_ARTIST_EXTRA ="com.sanmiaderibigbe.newgbedu.songArtistExtra"
    private const val SHOULD_LOAD_SONG_PAGE_INSTEAD_OF_ARTIST = "com.sanmiaderibigbe.newgbedu.shouldLoadSongPageInsteadOfArtist"

    fun initSongWebViewDetailScreenActivity(context: Context, songName: String, songArtist: String = "",shouldShowSongWebView : Boolean) : Intent{
        val intent = Intent(context, SongWebViewDetailScreenActivity::class.java )
        intent.putExtra(SONG_TITLE_EXTRA, songName)
        intent.putExtra(SONG_ARTIST_EXTRA, songArtist)
        intent.putExtra(SHOULD_LOAD_SONG_PAGE_INSTEAD_OF_ARTIST, shouldShowSongWebView)


        return intent
    }
}
}
