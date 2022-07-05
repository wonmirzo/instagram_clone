package com.wonmirzo.utils

import android.app.Activity
import android.util.Patterns
import android.widget.Toast

object Extensions {
    fun Activity.toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun CharSequence?.isValidEmail(): Boolean {
        return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun CharSequence?.isValidPassword(): Boolean {
        return !isNullOrEmpty() && length > 5
    }
}