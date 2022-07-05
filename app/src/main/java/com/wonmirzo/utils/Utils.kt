package com.wonmirzo.utils

import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.wonmirzo.R
import com.wonmirzo.model.ScreenSize
import com.wonmirzo.model.User
import com.wonmirzo.network.RetrofitHttp
import com.wonmirzo.network.model.Message
import com.wonmirzo.network.model.Notification
import com.wonmirzo.network.model.Response
import retrofit2.Call
import retrofit2.Callback

object Utils {

    fun getDeviceID(context: Context): String {
        val device_id: String =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return device_id
    }

    fun fireToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun screenSize(context: Application): ScreenSize {
        val displayMetrics = DisplayMetrics()
        val windowsManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceWidth = displayMetrics.widthPixels
        val deviceHeight = displayMetrics.heightPixels
        return ScreenSize(deviceWidth, deviceHeight)
    }

    fun dialogDouble(context: Context?, title: String?, callback: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_double)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        val d_cancel = dialog.findViewById<TextView>(R.id.d_cancel)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(true)
        }
        d_cancel.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(false)
        }
        dialog.show()
    }

    fun dialogSingle(context: Context?, title: String?, callback: DialogListener) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_dialog_single)
        dialog.setCanceledOnTouchOutside(true)
        val d_title = dialog.findViewById<TextView>(R.id.d_title)
        val d_confirm = dialog.findViewById<TextView>(R.id.d_confirm)
        d_title.text = title
        d_confirm.setOnClickListener {
            dialog.dismiss()
            callback.onCallback(true)
        }
        dialog.show()
    }

    fun sendNotification(context: Context, user: User, deviceToken: String) {
        val notification = Notification(
            context.getString(R.string.app_name),
            context.getString(R.string.str_followed_note).replace("$", user.fullName)
        )
        val deviceList = ArrayList<String>()
        deviceList.add(deviceToken)
        val message = Message(notification, deviceList)

        RetrofitHttp.notificationService.sendNotificationToUser(message)
            .enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if (!response.isSuccessful) {
                        Logger.d("@@@",response.body().toString())
                        Logger.d("@@@", "Error code: ${response.code()}")
                        return
                    }

                    Logger.d("@@@", response.message())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Logger.d("@@@", t.localizedMessage)
                }
            })
    }
}

interface DialogListener {
    fun onCallback(isChosen: Boolean)
}