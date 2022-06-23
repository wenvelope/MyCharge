package com.example.mycharge.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycharge.HomeActivity
import com.example.mycharge.R
import qiu.niorgai.StatusBarCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        instance=this
        if(HomeActivity.instance!=null){
            HomeActivity.instance!!.finish()
        }
        StatusBarCompat.translucentStatusBar(this,true)
    }
    companion object{
        var instance:MainActivity ?=null
    }
}