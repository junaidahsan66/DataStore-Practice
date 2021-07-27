package com.aexample.test_notifi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    companion object{
        const val MY_PREFS_NAME = "abc"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkForToken()){
            getToken()
        }
//        val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
//        intent.setClassName(
//            "com.miui.securitycenter",
//            "com.miui.permcenter.permissions.PermissionsEditorActivity"
//        )
//        intent.putExtra("extra_pkgname", packageName)
//        startActivity(intent)

    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("jun", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit()
            editor.putString("token",token)
            editor.apply()

            Log.d("jun", token.toString())
            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })    }

    private fun checkForToken(): Boolean {
        val prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
        val name = prefs.getString("token", "")
        Log.d("jun", "checkForToken: $name")
        return !name .equals("")
    }
}