package com.example.mycharge

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycharge.network.ChargeNetwork
import com.example.mycharge.network.Forms
import com.example.mycharge.network.MyForms
import kotlinx.android.synthetic.main.activity_forms.*
import kotlinx.coroutines.launch

class FormsActivity : AppCompatActivity() {
    private lateinit var formList:List<MyForms>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)
        lifecycleScope.launch {
            val sp = getSharedPreferences("userToken", Context.MODE_PRIVATE)
            val myToken = sp.getString("TOKEN","weifangzhou")
            val myForm = ChargeNetwork.getForms(myToken.toString())
            formList = myForm.forms.reversed()
            Log.e("wuhongru","$myToken  ${formList.toString()}")
            forms_list.post {
                forms_list.layoutManager = LinearLayoutManager(this@FormsActivity)
                forms_list.adapter = FormAdapter(formList)
            }

        }


    }
}