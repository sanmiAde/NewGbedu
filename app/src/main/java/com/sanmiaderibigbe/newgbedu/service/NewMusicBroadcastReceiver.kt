package com.sanmiaderibigbe.newgbedu.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

class NewMusicBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        val newMusicNotification = NewMusicNotification(context as Application)
//        newMusicNotification.createNotification()

        when {
            intent.action == null -> {

                NewMusicNotificationService.enqueueWork(context)
            }
            else -> {
                scheduleNewMusicNotificationAlarm(context)
            }
        }

    }

    companion object {
        private const val REQUEST_CODE = 1564
//        private const val INITIAL_DELAY = 5000
//        private const val PERIOD: Long =60000


        fun scheduleNewMusicNotificationAlarm(context: Context) {

            val (pendingIntent, alarmManager) = createPendingIntent(context)
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 9)
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

        }

        fun cancelNewMusicNotificationAlarm(context: Context){
            val (pendingIntent, alarmManager) = createPendingIntent(context)
            alarmManager.cancel(pendingIntent)
        }
        private fun createPendingIntent(ctxt: Context): Pair<PendingIntent?, AlarmManager> {
            val intent: Intent = Intent(ctxt, NewMusicBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(ctxt, REQUEST_CODE, intent, 0)
            val alarmManager: AlarmManager = ctxt.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return Pair(pendingIntent, alarmManager)

        }
    }
}