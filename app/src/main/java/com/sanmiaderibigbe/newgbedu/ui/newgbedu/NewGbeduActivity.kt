package com.sanmiaderibigbe.newgbedu.ui.newgbedu


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.sanmiaderibigbe.newgbedu.R
import kotlinx.android.synthetic.main.activity_main.*


class NewGbeduActivity : AppCompatActivity() {

    private val TAG: String = "mainSong"
    private lateinit var toolbar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getPermission()
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


    private fun getPermission() =
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

            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_MUSIC
                )


            }
        } else {

        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_MUSIC -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return
            }
        }
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
