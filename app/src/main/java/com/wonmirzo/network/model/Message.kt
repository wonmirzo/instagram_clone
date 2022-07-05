package com.wonmirzo.network.model

import com.google.gson.annotations.SerializedName

data class Message(
    val notification: Notification,
    @SerializedName("registration_ids")
    val registrationIds: ArrayList<String>
)

/*data class RegistrationId(
    val id: String
)*/

data class Notification(
    val title: String,
    val body: String
)

data class Response(
    @SerializedName("multicast_id")
    val multicastId: Long,
    val success: Int,
    val failure: Int,
    @SerializedName("canonical_ids")
    val canonicalIds: Int,
    val results: ArrayList<Result>
)

data class Result(
    @SerializedName("message_id")
    val messageId: String
)