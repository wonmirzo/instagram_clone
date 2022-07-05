package com.wonmirzo.model

class User {
    var uid: String = ""
    var fullName: String = ""
    var email: String = ""
    var password: String = ""
    var userImg: String = ""

    var deviceId = ""
    var deviceType = "A"
    var deviceToken = ""

    var isFollowed: Boolean = false

    constructor(fullName: String, email: String) {
        this.email = email
        this.fullName = fullName
    }

    constructor(fullName: String, email: String, userImg: String) {
        this.email = email
        this.fullName = fullName
        this.userImg = userImg
    }

    constructor(fullName: String, email: String, password: String, userImg: String) {
        this.email = email
        this.fullName = fullName
        this.userImg = userImg
        this.password = password
    }
}