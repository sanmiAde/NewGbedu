package com.sanmiaderibigbe.newgbedu.ui.newgbedu


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.Toast
import com.sanmiaderibigbe.newgbedu.R
import com.sanmiaderibigbe.newgbedu.data.Repository
import com.sanmiaderibigbe.newgbedu.data.local.LocalSong
import com.sanmiaderibigbe.newgbedu.data.remote.NetWorkState
import com.sanmiaderibigbe.newgbedu.service.NewMusicBroadcastReceiver
import com.sanmiaderibigbe.newgbedu.ui.SongWebViewDetailScreenActivity
import com.sanmiaderibigbe.newgbedu.ui.adapter.SongListDecoration
import com.sanmiaderibigbe.newgbedu.ui.adapter.SongsAdapter
import com.sanmiaderibigbe.newgbedu.ui.setting.SettingPreferenceActivity
import kotlinx.android.synthetic.main.fragment_new_gbedu.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_SHOULD_LOAD_ALL_SONGS = "shouldLoadALlSongsInsteadoFSongsBeingReleasedToday"


/**
 * A simple [Fragment] subclass.
 * Use the [NewGbeduFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class NewGbeduFragment : Fragment(), SongsAdapter.OpenSongWebView, SongsAdapter.OpenArtistWebView,  SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, p1: String?) {

        when(sharedPreferences?.getBoolean(getString(R.string.turn_on_daily_notification), false)){
            true -> {
                NewMusicBroadcastReceiver.scheduleNewMusicNotificationAlarm(context!! )
                Toast.makeText(activity, "New Music notification turned on.", Toast.LENGTH_SHORT).show()
                //TODO alamr fires immediately when notification is enabled after 9 o clock.
            }

            false -> {
                NewMusicBroadcastReceiver.cancelNewMusicNotificationAlarm(context!!)
                Toast.makeText(activity, "New Music notification turned off.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onClickArtist(song: LocalSong) {
        val intent = SongWebViewDetailScreenActivity.initSongWebViewDetailScreenActivity(context!!, songArtist =  song.artistName, songName = "", shouldShowSongWebView = false )
        startActivity(intent)
    }

    override fun onClickSongTitle(song: LocalSong) {
      val intent =  SongWebViewDetailScreenActivity.initSongWebViewDetailScreenActivity(context!!, song.songName, song.artistName, true )
        startActivity(intent)
    }

    // TODO: Rename and change types of parameters
    private var shouldLoadAllSongs: Boolean? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var viewModel: NewGbeduViewModel
    private lateinit var adapter: SongsAdapter

    private lateinit var prefListener : SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var pref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        //Todo fix rotration problem
          arguments?.let {
            shouldLoadAllSongs = it.getBoolean(ARG_SHOULD_LOAD_ALL_SONGS)

        }

        pref = PreferenceManager.getDefaultSharedPreferences(activity)
        pref.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_gbedu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NewGbeduViewModel::class.java)
        adapter = initRecyclerView(view)
        initSwipeToRefreshLayout(view)
        when(shouldLoadAllSongs){
            true -> {
                getNewSongs()
            }
            false -> {
                getSongReleasedToday()
                hideProgressBar()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        pref.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.new_gbedu_settings, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_settings -> {
                startActivity(Intent(activity, SettingPreferenceActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)


    }

    private fun initSwipeToRefreshLayout(view: View) {
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getNewSongsOnline()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getSongReleasedToday() {
        viewModel.getSongsCurrentlyReleasedToday().observe(this, Observer { it ->
            Log.d("main", it.toString())
            adapter.setTodoList(it)
            //Todo add empty state screen
        })
    }

    private fun initRecyclerView(view: View): SongsAdapter {
        val adapter = SongsAdapter(context!!, this, this)
        val recyclerView = view.findViewById<RecyclerView>(R.id.song_recyler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        val largePadding = resources.getDimensionPixelSize(R.dimen.spacing_big)
        val smallPadding = resources.getDimensionPixelSize(R.dimen.spacing_small)
        recyclerView.addItemDecoration(SongListDecoration(largePadding, smallPadding))
        return adapter
    }


    private fun observeNetworkState() {
        viewModel.getNetworkstate().observe(this, Observer { network: NetWorkState? ->

            when (network) {
                is NetWorkState.NotLoaded -> {

                    Toast.makeText(activity, "Not loaded", Toast.LENGTH_SHORT).show()
                }

                is NetWorkState.Loading -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
                    showProgressBar()
                }

                is NetWorkState.Success -> {
                    hideProgressBar()
                    Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show()
                }

                is NetWorkState.Error -> {
                    val netWorkError: NetWorkState.Error = network
                    Toast.makeText(activity, netWorkError.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
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
        val connectivityManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun getNewSongsData() {

        viewModel.getNewSongsOnline().observe(this, Observer { it ->
            Log.d("main", it.toString())
            adapter.setTodoList(it)
        })

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param shouldLoadAllSongs This determines the type of data that is shown on the screen.
         * instead of creating a new activity and fragment to display songs being released on a specifid date.

         * @return A new instance of fragment NewGbeduFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(shouldLoadAllSongs: Boolean) =
            NewGbeduFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOULD_LOAD_ALL_SONGS, shouldLoadAllSongs)

                }
            }
    }
}
