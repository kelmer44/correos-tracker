package net.kelmer.correostracker.initializer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import net.kelmer.correostracker.AppInitializer
import net.kelmer.correostracker.R
import net.kelmer.correostracker.service.worker.ParcelPollWorker
import javax.inject.Inject

class NotificationInitializer @Inject constructor() : AppInitializer {
    override fun initialize(application: Application) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = application.getString(R.string.channel_name)
            val descriptionText = application.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ParcelPollWorker.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
