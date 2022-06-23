package com.example.mycharge


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycharge.login.MainActivity
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_charge.*
import qiu.niorgai.StatusBarCompat


// sp name "userToken"
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        instance=this
        MainActivity.instance?.finish()
         val wic = ViewCompat.getWindowInsetsController(window.decorView);
         wic?.isAppearanceLightStatusBars = true

        StatusBarCompat.translucentStatusBar(this,true);
        //绑定底部导航栏和fragment
        val myNaviControl:NavController = findNavController(R.id.navi_hostfragment)
        navi_bottom.setupWithNavController(myNaviControl)


    }

    companion object{
        var instance:HomeActivity ?=null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            200 -> {
                val hmsScan: HmsScan? = data?.getParcelableExtra(ScanUtil.RESULT) // 获取扫码结果 ScanUtil.RESULT
                if (!TextUtils.isEmpty(hmsScan?.getOriginalValue())) {
                    saoma.text= hmsScan?.getOriginalValue()
                }
                super.onActivityResult(requestCode, resultCode, data)
            }


        }

    }

}