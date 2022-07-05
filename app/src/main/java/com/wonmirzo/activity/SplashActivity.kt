package com.wonmirzo.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.WindowManager
import com.google.firebase.messaging.FirebaseMessaging
import com.wonmirzo.R
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.PrefsManager
import com.wonmirzo.utils.Logger

/*
* In SplashActivity user can visit to SignInActivity or MainActivity
*/
@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    private val TAG = SplashActivity::class.java.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        initViews()
    }

    private fun initViews() {
        countDownTimer()
        loadFCMToken()
    }

    private fun countDownTimer() {
        object : CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                if (AuthManager.isSignedIn()) {
                    callMainActivity(this@SplashActivity)
                } else {
                    callSignInActivity(this@SplashActivity)
                }
            }
        }.start()
    }

    private fun loadFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Logger.d(TAG, "Fetching FCM registration token failed")
                return@addOnCompleteListener
            }
            // Get new FCM registration token
            // Save it in locally to user late
            val token = task.result
            Logger.d(TAG, token.toString())
             PrefsManager(this).storeDeviceToken(token.toString())
        }
    }
}