package com.wonmirzo.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.wonmirzo.utils.Logger

class FCMService : FirebaseMessagingService() {
    val TAG = FCMService::class.java.simpleName

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.i(TAG, "Refreshed toke :: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Logger.i(TAG, "Message :: $message")
    }
}