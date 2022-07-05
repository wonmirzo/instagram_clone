package com.wonmirzo.model

import java.text.SimpleDateFormat
import java.util.*

class Post {
    var id: String = ""
    var caption: String = ""
    var postImg: String = ""
    var currentDate: String = ""

    var uid: String = ""
    var fullName: String = ""
    var userImg: String = ""

    var deviceToken: String = ""

    var isLiked = false

    constructor(caption: String, postImg: String) {
        this.caption = caption
        this.postImg = postImg
    }

    constructor(id: String, caption: String, postImg: String) {
        this.id = id
        this.caption = caption
        this.postImg = postImg
    }

    fun setCurrentTime() {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        currentDate = sdf.format(Date())
    }
}