package com.wonmirzo.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import com.wonmirzo.R
import com.wonmirzo.activity.SignInActivity

/*
*  BaseFragment is parent for all Fragments
* */
open class BaseFragment : Fragment() {
    private var progressDialog: AppCompatDialog? = null

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    fun showLoading(activity: Activity?) {
        if (activity == null) return

        if (progressDialog != null && progressDialog!!.isShowing) {

        } else {
            progressDialog = AppCompatDialog(activity, R.style.CustomDialog)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog!!.setContentView(R.layout.custom_progress_dialog)
            val ivProgress = progressDialog!!.findViewById<ImageView>(R.id.iv_progress)
            val animationDrawable = ivProgress!!.drawable as AnimationDrawable
            animationDrawable.start()
            if (!activity.isFinishing) progressDialog!!.show()
        }
    }

    protected fun dismissLoading() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    fun callSignInActivity(activity: Activity) {
        val intent = Intent(context, SignInActivity::class.java)
        startActivity(intent)
        activity.finish()
    }
}