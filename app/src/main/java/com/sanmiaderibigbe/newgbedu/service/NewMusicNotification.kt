package com.sanmiaderibigbe.newgbedu.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.sanmiaderibigbe.newgbedu.R
import com.sanmiaderibigbe.newgbedu.ui.newgbedu.NewGbeduActivity


class NewMusicNotification(private val application: Application) {

    private lateinit var notificationManager: NotificationManager

    private fun createNotificationChannel() {
        notificationManager = application.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        when {
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O -> {
                val notificationChannel = NotificationChannel(
                    NEW_SONG_NOTIFICATION_CHANNEL_ID,
                    "New Music Notification", NotificationManager
                        .IMPORTANCE_HIGH
                )
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.RED
                notificationChannel.enableVibration(true)
                notificationChannel.description = "Daily Music Notification from New Gbedu"
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }

    }

    private fun getNotificationBuilder(numberOfArtistsReleasedToday : Int): NotificationCompat.Builder {
        createNotificationChannel()

        return NotificationCompat.Builder(application, NEW_SONG_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(application.getString(R.string.new_gbedu))
            .setContentText("$numberOfArtistsReleasedToday are being released today.")
            .setContentIntent(getNewMusicPendingIntent())
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
    }

    /**
     * @param numberOfArtistsReleasedToday THe number of artists being released today.
     */
    fun createNotification(numberOfArtistsReleasedToday : Int) {
        val notifyBuilder = getNotificationBuilder(numberOfArtistsReleasedToday)
        notificationManager.notify(NEW_MUSIC_NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun getNewMusicPendingIntent(): PendingIntent {

        val notificationIntent = Intent(application, NewGbeduActivity::class.java)
        return PendingIntent.getActivity(
            application,
            NEW_MUSIC_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    companion object {
        private const val NEW_SONG_NOTIFICATION_CHANNEL_ID = "new_song_notification_channel"
        private const val NEW_MUSIC_NOTIFICATION_ID = 12378
    }
}