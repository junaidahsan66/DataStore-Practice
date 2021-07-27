package com.aexample.test_notifi

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FcmMessageService : FirebaseMessagingService()  {

    override fun onCreate() {
        super.onCreate()
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        try {
            //{data={"user_id":10000255,"id":345,"state":"new","type":"ORDER_UPDATE","user":"seller"}
            //notification={"title":"Order, NDH-202000000316-1","body":"New order placed."}}
            val params = remoteMessage.data
            println("data =" + remoteMessage.data.toString())
            if (!remoteMessage.data.isNullOrEmpty()){
                val intent = Intent(this, MyService::class.java)
                intent.action = "ACTION_INCOMING_CALL"

                startService(intent)
            }
            println("notification =" + remoteMessage.notification.toString())
            println("data.data =" + remoteMessage.data.get("data").toString())
            println("data.notification =" + remoteMessage.data.get("notification").toString())

            val dataObj = JSONObject(params as Map<*, *>)
            Log.e("jun", "notification_payload$dataObj")
            val noti = remoteMessage.notification
            Log.e("jun", "notification_payload_N"+noti.toString())
            val data = remoteMessage.data
            Log.e("jun", "notification_payload_D"+ data.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

}