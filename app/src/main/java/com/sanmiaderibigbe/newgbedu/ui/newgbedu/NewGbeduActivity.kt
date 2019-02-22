package com.sanmiaderibigbe.newgbedu.ui.newgbedu

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.sanmiaderibigbe.newgbedu.R
import com.sanmiaderibigbe.newgbedu.data.remote.NetWorkState
import com.sanmiaderibigbe.newgbedu.ui.adapter.SongListDecoration
import com.sanmiaderibigbe.newgbedu.ui.adapter.SongsAdapter
import kotlinx.android.synthetic.main.activity_main.*

class NewGbeduActivity : AppCompatActivity() {


    private lateinit var viewModel: NewGbeduViewModel
    private lateinit var adapter: SongsAdapter
    private val TAG: String = "mainSong"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(NewGbeduViewModel::class.java)
        getPermission()
        adapter = initRecyclerView()
        getNewSongs()
        //Todo fix rotration problem


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun initRecyclerView(): SongsAdapter {
        val adapter = SongsAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.song_recyler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        val largePadding = resources.getDimensionPixelSize(R.dimen.spacing_big)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.spacing_small)
        recyclerView.addItemDecoration(SongListDecoration(largePadding, smallPadding))
        return adapter
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun getNewSongs() {
        when {
            isNetworkAvailable() -> {
                observeNetworkState()
                getNewSongsData()

            }
            else -> {
                showProgressBar()
                getSongsOffline(adapter)
                hideProgressBar()
            }
        }


    }

    private fun getSongsOffline(adapter: SongsAdapter) {
        viewModel.getCache().observe(this, Observer { it ->
            adapter.setTodoList(it)
        })
    }

    private fun getPermission() =// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_MUSIC
                )

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            //getArtistList()
        }


//    private fun getArtistListFromPhone() {
//
//        viewModel.getArtistsOnPhoneList().observe(this, Observer { artist ->
//            Log.d("Art", artist.toString())
//            artist?.forEach {
//                //artists.append("$it \n")
//            }
//        })
//    }


    private fun observeNetworkState() {
        viewModel.getNetworkstate().observe(this, Observer { network: NetWorkState? ->

            when (network) {
                is NetWorkState.NotLoaded -> {

                    Toast.makeText(this, "Not loaded", Toast.LENGTH_SHORT).show()
                }

                is NetWorkState.Loading -> {
                    Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    showProgressBar()
                }

                is NetWorkState.Success -> {
                    hideProgressBar()
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                }

                is NetWorkState.Error -> {
                    val netWorkError: NetWorkState.Error = network
                    Toast.makeText(this, netWorkError.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //Turn this to bindingadapter logic.
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        song_recyler_view.visibility = View.INVISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        song_recyler_view.visibility = View.VISIBLE
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getNewSongsData() {

        viewModel.getNewSongsOnline().observe(this, Observer { it ->
            Log.d("main", it.toString())
            adapter.setTodoList(it)
        })

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_MUSIC -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, do your work....
                    //getArtistList()

                } else {
                    // permission denied
                    // Disable the functionality that depends on this permission.
                }
                return
            }
        }// other 'case' statements for other permssions
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_MUSIC: Int = 2324
    }




}
