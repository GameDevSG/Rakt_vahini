package com.example.raktvahini.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.raktvahini.R

/**
 * Utility class to handle App Notifications.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "donation_channel"
    private const val CHANNEL_NAME = "Donation Updates"
    private const val CHANNEL_DESC = "Notifications for successful donations"

    /**
     * Creates a Notification Channel. Required for Android 8.0 (API 26) and above.
     */
    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Sends a "Thank You" notification to the user.
     */
    fun sendThankYouNotification(context: Context) {
        // 1. Always ensure the channel is created first
        createNotificationChannel(context)

        // 2. Build the notification content
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_blood_drop) // Using the blood drop icon we created earlier
            .setContentTitle("Rakt-Vahini")
            .setContentText("Thank you for saving lives ❤️")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Notification disappears when clicked

        // 3. Show the notification
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Use a unique ID (like 1) to identify this notification
        notificationManager.notify(1, builder.build())
    }
}
