package com.example.mycharge.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mycharge.HomeActivity
import com.example.mycharge.R
import com.example.mycharge.login.MainActivity
import com.kongzue.dialogx.dialogs.MessageDialog
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener
import kotlinx.android.synthetic.main.fragment_mine.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MineFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var fathercontext:HomeActivity ?=null
    private lateinit var mysp:SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fathercontext = context as HomeActivity
        //初始化userToken
        // sp
        mysp = fathercontext!!.getSharedPreferences("userToken",Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username.text = mysp.getString("USERNAME","无用户")



        logOut.setOnClickListener {
            MessageDialog.show("主人","您真的要退出登录吗","确定","取消").apply {
                setOkButton("确定",object :OnDialogButtonClickListener<MessageDialog>{
                    override fun onClick(baseDialog: MessageDialog?, v: View?): Boolean {
                        val editor = mysp.edit()
                        editor.putString("TOKEN","weifangzhou")
                        editor.commit()
                        val intent = Intent(fathercontext,MainActivity::class.java)
                        startActivity(intent)
                        return false
                    }
                })
            }

        }
    }





    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MineFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }






}