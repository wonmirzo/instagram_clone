package com.wonmirzo.network

import com.wonmirzo.network.services.NotificationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHttp {
    const val IS_TESTER: Boolean = true

    private const val SERVER_DEVELOPMENT = "https://fcm.googleapis.com"
    private const val SERVER_PRODUCTION = "https://fcm.googleapis.com/fcm/"

    private fun server(): String {
        return if (IS_TESTER) SERVER_DEVELOPMENT
        else SERVER_PRODUCTION
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(server())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val notificationService: NotificationService = retrofit.create(NotificationService::class.java)
}