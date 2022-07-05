package com.wonmirzo.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wonmirzo.R
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.DatabaseManager
import com.wonmirzo.manager.PrefsManager
import com.wonmirzo.manager.handler.AuthHandler
import com.wonmirzo.manager.handler.DBUserHandler
import com.wonmirzo.model.User
import com.wonmirzo.utils.Extensions.toast
import com.wonmirzo.utils.Utils
import java.lang.Exception

/*
* In SignUpActivity user can signup using fullName, email, password
* */
class SignUpActivity : BaseActivity() {
    private val TAG = SignUpActivity::class.java.toString()
    private lateinit var etFullName: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etCPassword = findViewById(R.id.etCPassword)

        val btnSignUp: Button = findViewById(R.id.btnSignUp)
        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                val user = User(fullName, email, password, "")
                firebaseSignUp(user)
            }
        }
        val tvSignIn: TextView = findViewById(R.id.tvSignIn)
        tvSignIn.setOnClickListener { finish() }
    }

    private fun firebaseSignUp(user: User) {
        showLoading(this)
        AuthManager.signUp(user.email, user.password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                user.uid = uid
                storeUserToDB(user)
                toast(getString(R.string.str_signup_success))
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signup_failed))
            }
        })
    }

    private fun storeUserToDB(user: User) {
        user.deviceToken = PrefsManager(this).loadDeviceToken()!!
        user.deviceId = Utils.getDeviceID(this)

        DatabaseManager.storeUser(user, object : DBUserHandler {
            override fun onSuccess(user: User?) {
                dismissLoading()
                callMainActivity(context)
            }

            override fun onError(e: Exception) {

            }
        })
    }
}