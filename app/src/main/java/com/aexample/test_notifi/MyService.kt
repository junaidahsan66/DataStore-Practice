package com.aexample.test_notifi

import android.annotation.TargetApi
import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.core.app.NotificationCompat

class MyService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.action
        if (action != null) {
            val notificationId = intent.getIntExtra("INCOMING_CALL_NOTIFICATION_ID", 0)
            when (action) {
                "ACTION_INCOMING_CALL" -> handleIncomingCall(notificationId)
               
                else -> {
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun handleIncomingCall(notificationId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            
            startForeground(
                notificationId,
                createNotification( notificationId, NotificationManager.IMPORTANCE_HIGH)
            )
        }
        if (Build.VERSION.SDK_INT >= 29 ) {
            return
        }
        val intent = Intent(this, CallActivity::class.java)
        intent.action = "ACTION_INCOMING_CALL"
        intent.putExtra("INCOMING_CALL_NOTIFICATION_ID", notificationId)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(intent)    }



    private fun createNotification(
        notificationId: Int,
        channelImportance: Int
    ): Notification? {
        val intent = Intent(this, CallActivity::class.java)
        intent.action = "ACTION_INCOMING_CALL_NOTIFICATION"
        intent.putExtra("INCOMING_CALL_NOTIFICATION_ID", notificationId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        /*
         * Pass the notification id and call sid to use as an identifier to cancel the
         * notification later
         */

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildNotification(
                pendingIntent,
                notificationId,
                createChannel(channelImportance)
            )
        } else {
            NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(" is calling.")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setGroup("test_app_notification")
                .setColor(Color.rgb(214, 10, 37)).build()
        }
    }

    /**
     * Build a notification.
     *
     * @param text          the text of the notification
     * @param pendingIntent the body, pending intent for the notification
     * @param extras        extras passed with the notification
     * @return the builder
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun buildNotification(
       pendingIntent: PendingIntent,
        notificationId: Int,
        channelId: String
    ): Notification? {
        val rejectIntent = Intent(
            applicationContext,
            MyService::class.java
        )
        rejectIntent.action = "ACTION_REJECT"
        rejectIntent.putExtra("INCOMING_CALL_NOTIFICATION_ID", notificationId)
        val piRejectIntent = PendingIntent.getService(
            applicationContext,
            0,
            rejectIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val acceptIntent = Intent(
            applicationContext,
            MyService::class.java
        )
        acceptIntent.action = "ACTION_ACCEPT"
        acceptIntent.putExtra("INCOMING_CALL_NOTIFICATION_ID", notificationId)
        val piAcceptIntent = PendingIntent.getService(
            applicationContext,
            0,
            acceptIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: Notification.Builder = Notification.Builder(
            applicationContext, channelId
        )
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Hello")
            .setCategory(Notification.CATEGORY_CALL)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_delete,
                getString(R.string.app_name),
                piRejectIntent
            )
            .addAction(android.R.drawable.ic_menu_call, getString(R.string.app_name), piAcceptIntent)
            .setFullScreenIntent(pendingIntent, true)
        return builder.build()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(channelImportance: Int): String {
        var callInviteChannel = NotificationChannel(
            "VOICE_CHANNEL_HIGH_IMPORTANCE",
            "Primary Voice Channel", NotificationManager.IMPORTANCE_HIGH
        )
        var channelId: String = "VOICE_CHANNEL_HIGH_IMPORTANCE"
        if (channelImportance == NotificationManager.IMPORTANCE_LOW) {
            callInviteChannel = NotificationChannel(
               "VOICE_CHANNEL_LOW_IMPORTANCE",
                "Primary Voice Channel", NotificationManager.IMPORTANCE_LOW
            )
            channelId = "VOICE_CHANNEL_LOW_IMPORTANCE"
        }
        callInviteChannel.lightColor = Color.GREEN
        callInviteChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(callInviteChannel)
        return channelId
    }

}