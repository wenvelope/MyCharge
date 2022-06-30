package com.example.mycharge

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.kongzue.dialogx.DialogX
import com.kongzue.dialogx.style.IOSStyle

class MyApplication:Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context=applicationContext
        DialogX.init(this)
        DialogX.globalStyle = IOSStyle()
    }
}