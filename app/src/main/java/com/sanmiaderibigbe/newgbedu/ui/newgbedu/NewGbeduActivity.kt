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

import com.sanmiaderibigbe.newgbedu.R

import kotlinx.android.synthetic.main.activity_main.*


import android.support.v4.app.Fragment

import android.support.v7.app.ActionBar


class NewGbeduActivity : AppCompatActivity() {

    private val TAG: String = "mainSong"
    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()

        //Todo fix rotration problem
        toolbar = supportActionBar!!
        toolbar.title = "Latest  Gbedu"
        loadFragment(NewGbeduFragment.newInstance(true))


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                //Todo optimise this process. Currently button opens a new fragment eventhough the fragment is currently open
                toolbar.title = "Latest  Gbedu"
                loadFragment(NewGbeduFragment.newInstance(true))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                toolbar.title = "Gbedu released today"
                loadFragment(NewGbeduFragment.newInstance(false))
                return@OnNavigationItemSelectedListener true
            }

        }
        false
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

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragement_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_MUSIC: Int = 2324
    }

}
