package com.sanmiaderibigbe.newgbedu.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class NewMusicBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        val newMusicNotification = NewMusicNotification(context as Application)
//        newMusicNotification.createNotification()

        when {
            intent.action == null -> {
                NewMusicNotificationService.enqueueWork(context)
            }
            else -> {
                scheduleAlarms(context)
            }
        }

    }

    companion object {
        private const val REQUEST_CODE = 1564
        private const val INITIAL_DELAY = 5000
        private const val PERIOD: Long = 900000

        fun scheduleAlarms(ctxt: Context) {
            val alarmManager: AlarmManager = ctxt.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent: Intent = Intent(ctxt, NewMusicBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(ctxt, REQUEST_CODE, intent, 0)

            alarmManager.setRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_DELAY,
                PERIOD,
                pendingIntent
            )
        }
    }
}