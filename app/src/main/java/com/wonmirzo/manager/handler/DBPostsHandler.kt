package com.wonmirzo.manager.handler

import com.wonmirzo.model.Post

interface DBPostsHandler {
    fun onSuccess(posts: ArrayList<Post>)
    fun onError(e: Exception)
}