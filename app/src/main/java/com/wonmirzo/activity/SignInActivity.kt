package com.wonmirzo.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.wonmirzo.R
import com.wonmirzo.manager.AuthManager
import com.wonmirzo.manager.handler.AuthHandler
import com.wonmirzo.utils.Extensions.toast

/*
* In SignUpActivity user can login using email, password
*/
class SignInActivity : BaseActivity() {
    private val TAG = SignInActivity::class.java.toString()

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initViews()
    }

    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        val btnSignIn: Button = findViewById(R.id.btnSignIn)
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseSignIn(email, password)
            }
        }

        val tvSignUp: TextView = findViewById(R.id.tvSignUp)
        tvSignUp.setOnClickListener { callSignUpActivity() }
    }

    private fun firebaseSignIn(email: String, password: String) {
        showLoading(this)
        AuthManager.signIn(email, password, object : AuthHandler {
            override fun onSuccess(uid: String) {
                dismissLoading()
                toast(getString(R.string.str_signin_success))
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                dismissLoading()
                toast(getString(R.string.str_signin_failed))
            }
        })
    }

    private fun callSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}