package com.sanmiaderibigbe.newgbedu.service

import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService

class NewMusicNotificationService : JobIntentService() {

    override fun onHandleWork(p0: Intent) {
        val newMusicNotification = NewMusicNotification(applicationContext as Application)
        newMusicNotification.createNotification()
    }

    companion object {
        private const val UNIQUE_JOB_ID = 1323

        fun enqueueWork(ctxt: Context) {
            enqueueWork(
                ctxt,
                NewMusicNotificationService::class.java,
                UNIQUE_JOB_ID,
                Intent(ctxt, NewMusicNotificationService::class.java)
            )
        }
    }

}
