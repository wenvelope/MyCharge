package com.example.mycharge.main


import android.Manifest.permission_group.LOCATION
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mycharge.HomeActivity
import com.example.mycharge.MyApplication
import com.example.mycharge.R
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.fragment_charge.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChargeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mycontext :HomeActivity
    private lateinit var saoyisao :Button
    private var myView:View ?=null

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
        if(myView==null){
            myView=inflater.inflate(R.layout.fragment_charge, container, false)
            return myView
        }else{
            return myView
        }



    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mycontext = context as HomeActivity
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saoyisao = view.findViewById(R.id.saoyisao)
        if(car_price.text.isNullOrEmpty()){
            car_status.text="未连接"
        }
        Glide.with(MyApplication.context).load(R.drawable.ic_charging).into(charge_image)
        Glide.with(MyApplication.context).load(R.drawable.ic_connect).into(connect_image)
        if(charge_status.text.toString()=="未开启充电"){
            Glide.with(MyApplication.context).load(R.drawable.chargestop).into(car_charge)
        }else{
            Glide.with(MyApplication.context).load(R.drawable.carcharge).into(car_charge)
        }

        saoyisao.setOnClickListener {

            PermissionX.init(mycontext)
                .permissions(android.Manifest.permission.CAMERA)
                .request{ allgranted,grantedlist,deniedlist->
                    if (!allgranted){
                        Toast.makeText(MyApplication.context,"部分功能无法使用", Toast.LENGTH_SHORT).show()
                    }
                }


            val option = HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
            ScanUtil.startScan(
                mycontext,200,option
            )
        }
        touxiang.setOnClickListener {
            findNavController().navigate(R.id.action_chargeFragment_to_mineFragment)
        }

//        switch_button.setEnableEffect(false)


        charge_switch_button.setOnCheckedChangeListener { view, isChecked ->
            when(isChecked){
                true -> {
                    if(car_status.text.toString() == "已连接"){
                        charge_status.text = "已开启充电"
                        TipDialog.show("开始充电", WaitDialog.TYPE.SUCCESS)
                        Glide.with(MyApplication.context).load(R.drawable.carcharge).into(car_charge)
                    }else{
                        charge_status.text="未开启充电"
                        //异步改变状态
                        charge_switch_button.post { charge_switch_button.toggle() }
                        //抛出提示
                        TipDialog.show("未连接充电桩", WaitDialog.TYPE.WARNING)
                    }
                }
                false ->{
                    charge_status.text="未开启充电"
                    Glide.with(MyApplication.context).load(R.drawable.chargestop).into(car_charge)
                }
            }
        }

        connect_switch_button.setOnCheckedChangeListener{view,isChecked ->
            when(isChecked){
                true ->{
                    if(car_status.text.toString() == "已连接"){
                        connect_status.text = "已连接充电桩"
                        TipDialog.show("已连接充电桩", WaitDialog.TYPE.SUCCESS)
                    }else{
                        connect_status.text = "未连接充电桩"
                        connect_switch_button.post { connect_switch_button.toggle() }
                        TipDialog.show("未连接充电桩",WaitDialog.TYPE.ERROR)
                    }
                }
                false ->{
                    if(charge_status.text.toString()=="已开启充电"){
                        charge_switch_button.toggle()
                        charge_status.text = "未开启充电"
                    }
                    connect_status.text = "未连接充电桩"
                    car_status.text="未连接"
                    car_power.text = ""
                    car_price.text = ""
                    Glide.with(MyApplication.context).load(R.drawable.chargestop).into(car_charge)
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
////        if (resultCode != RESULT_OK || data == null) {
////            return
////        }
//        when (requestCode) {
//            200 -> {
//                val hmsScan: HmsScan? = data?.getParcelableExtra(ScanUtil.RESULT) // 获取扫码结果 ScanUtil.RESULT
//                if (!TextUtils.isEmpty(hmsScan?.getOriginalValue())) {
//                     saoma.text= hmsScan?.getOriginalValue()
//                }
//            }
//
//        }
//    }





    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ChargeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}