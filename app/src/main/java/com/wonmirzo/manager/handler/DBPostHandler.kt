package com.wonmirzo.manager.handler

import com.wonmirzo.model.Post

interface DBPostHandler {
    fun onSuccess(post: Post)
    fun onError(e: Exception)
}