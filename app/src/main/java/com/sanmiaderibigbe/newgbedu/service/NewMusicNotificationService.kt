package com.sanmiaderibigbe.newgbedu.service


import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService
import android.util.Log
import com.sanmiaderibigbe.newgbedu.data.Repository

class NewMusicNotificationService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        val repository = Repository(application)
        val listOfSongBeingReleased =  repository.getSongsReleasedToday()
        //when {
           // listOfSongBeingReleased.isNotEmpty() -> {
                val notification = NewMusicNotification(application)
                Log.d(TAG,listOfSongBeingReleased.size.toString() )
                notification.createNotification(listOfSongBeingReleased.size)
          //  }
       // }
    }

    companion object {
        private const val UNIQUE_JOB_ID = 1323

        fun enqueueWork(ctxt: Context) {
            val intent = Intent(ctxt, NewMusicNotificationService::class.java)

            enqueueWork(
                ctxt,
                NewMusicNotificationService::class.java,
                UNIQUE_JOB_ID,
                intent

            )
        }

        val TAG : String = "NotificationService"
    }

}
