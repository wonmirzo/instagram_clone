package com.wonmirzo.manager.handler

interface DBFollowHandler {
    fun onSuccess(isDone: Boolean)
    fun onError(e: Exception)
}