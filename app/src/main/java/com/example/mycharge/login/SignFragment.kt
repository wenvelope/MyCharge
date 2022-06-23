package com.example.mycharge.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mycharge.MyApplication
import com.example.mycharge.R
import com.example.mycharge.network.ChargeNetwork
import com.example.mycharge.network.NetworkTools
import com.kongzue.dialogx.dialogs.PopTip
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_sign.*
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class SignFragment: Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_sign, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign.setOnClickListener{
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()
            val confirm  = editPassword_ack.text.toString()
            if(username.isEmpty()||password.isEmpty()||confirm.isEmpty()){
                    Toasty.error(MyApplication.context,"用户名或者密码为空",Toast.LENGTH_SHORT,true).show()
            }else if (password != confirm){
                Toasty.info(MyApplication.context,"两次密码不一致",Toast.LENGTH_SHORT,true).show()
            } else if(password.length<8){
                PopTip.show("密码位数应大于8")
            }
            else{
                lifecycleScope.launch {
                    if(NetworkTools.isNetworkAvailable(MyApplication.context)){
                        val message = ChargeNetwork.Sign(username,password).string()
                        if(message == "ok"){
                            Toasty.success(MyApplication.context,"注册成功",Toast.LENGTH_SHORT,true).show()
                            it.findNavController().popBackStack()
                        }else if(message=="error"){
                            Toasty.info(MyApplication.context,"用户已经存在",Toast.LENGTH_SHORT,true).show()
                        }
                    }else{
                        Toasty.error(MyApplication.context,"无网络连接",Toast.LENGTH_SHORT,true).show()
                    }

                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}