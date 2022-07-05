package com.wonmirzo.network.services

import com.wonmirzo.network.model.Message
import com.wonmirzo.network.model.Response
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization:key=AAAA1IQfr4A:APA91bHUrZ6EXNmCxfXIW9DZgMwWc_An-i-wcF2FUE5GfqYEzbVQE1zvkFe854-4gY_ekzq2Ffmx5-2NaAs-5F10x5fkEWc7CZpnQBzFZt8gWVMBVa2Z1xh9Zm1Kwpo1q7ZdwASwcceF")
    @POST("/fcm/send")
    fun sendNotificationToUser(@Body message: Message): Call<Response>
}