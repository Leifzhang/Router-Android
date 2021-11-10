package com.kronos.sample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 *
 *  @Author LiABao
 *  @Since 2021/10/14
 *
 */
object NotificationSample {

    fun show(context: Context, text: String = "textTitle"): NotificationCompat.Builder {
        registerChannelId(context, CHANNEL_ID)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("textTitle")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        return builder
    }

    const val CHANNEL_ID = "123415"

    private fun registerChannelId(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }
        val nm: NotificationManager? = getNotificationManager(context)
        if (nm != null) {
            val channel = NotificationChannel(
                channelId, "bilibili", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "description"
            channel.setSound(null, null)
            nm.createNotificationChannel(channel)
        }
    }


    fun getNotificationManager(context: Context): NotificationManager? {
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE)
            ?: return null
        return service as NotificationManager
    }


}
