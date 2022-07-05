package com.wonmirzo.manager.handler

import com.wonmirzo.model.User

interface DBUserHandler {
    fun onSuccess(user: User? = null)
    fun onError(e: Exception)
}