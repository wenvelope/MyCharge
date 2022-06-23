package com.example.mycharge.login

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.mycharge.HomeActivity
import com.example.mycharge.MyApplication
import com.example.mycharge.R
import com.example.mycharge.network.ChargeNetwork
import com.example.mycharge.network.NetworkTools
import com.kongzue.dialogx.dialogs.PopTip
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var mfathercontext:Context? = null
    private var mysp :SharedPreferences?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mfathercontext=context;
    }
//用户名输入框id  editTextPhone2
    //密码输入框id  editTextTextPassword
    //注册按钮id sign
    //登录按钮id login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login.setOnClickListener{
            val phoneNumber = editUsername.text.toString()
            val password = editPassword.text.toString()
            if(phoneNumber.isEmpty()||password.isEmpty()){
                PopTip.show("用户名或密码为空")
            }else{
                //登录逻辑
                val fatherActivity = mfathercontext as MainActivity
                mysp = fatherActivity.getSharedPreferences("userToken",Context.MODE_PRIVATE)
                val  mintent = Intent(fatherActivity,HomeActivity::class.java)
                lifecycleScope.launch {
                        if(NetworkTools.isNetworkAvailable(MyApplication.context)){
                            val myToken = ChargeNetwork.getToken(phoneNumber,password).string()
                            if(myToken == "error"){
                                Toasty.error(MyApplication.context,"用户名或密码错误",Toast.LENGTH_SHORT,true).show()
                                Log.e("wuhongru","error")
                            }else{
                                //保存用户唯一TOKEN
                                val editor =mysp!!.edit()
                                editor.putString("TOKEN",myToken)
                                editor.putString("USERNAME",phoneNumber)
                                editor.putString("PASSWORD",password)
                                editor.commit()
                                Toasty.success(MyApplication.context,"欢迎你$phoneNumber",Toast.LENGTH_SHORT,true).show()
                                startActivity(mintent)
                            }
                        }else{
                            Toasty.error(MyApplication.context,"无网络连接",Toast.LENGTH_SHORT,true).show()
                        }

                }

            }


        }
        sign.setOnClickListener{
            //注册逻辑 跳转到注册页面
            it.findNavController().navigate(R.id.action_loginFragment_to_SignFragment)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}