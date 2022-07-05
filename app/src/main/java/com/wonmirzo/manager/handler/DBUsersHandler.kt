package com.wonmirzo.manager.handler

import com.wonmirzo.model.User

interface DBUsersHandler {
    fun onSuccess(users: ArrayList<User>)
    fun onError(e: Exception)
}