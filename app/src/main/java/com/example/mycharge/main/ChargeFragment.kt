package com.example.mycharge.main


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.mycharge.HomeActivity
import com.example.mycharge.MyApplication
import com.example.mycharge.R
import com.example.mycharge.network.ChargeNetwork
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.kongzue.dialogx.dialogs.TipDialog
import com.kongzue.dialogx.dialogs.WaitDialog
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.fragment_charge.*
import kotlinx.android.synthetic.main.fragment_charge.view.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChargeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mycontext :HomeActivity
    private lateinit var saoyisao :Button
    private var myView:View ?=null
    private lateinit var sp:SharedPreferences

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
        sp = mycontext.getSharedPreferences("userToken",Context.MODE_PRIVATE)
    }



    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saoyisao = view.findViewById(R.id.saoyisao)
        if(status_forms.text.isNullOrEmpty()){
            lifecycleScope.launch {
                val myToken = sp.getString("TOKEN","weifangzhou")
                val myForm = ChargeNetwork.getForms(myToken.toString())
                val formList = myForm.forms.reversed()
                if(formList.isNotEmpty()){
                    status_forms.post { status_forms.text = "?????????" }
                    start_time.post { start_time.text = formList[0].start }
                    consume_time.post {
                        val consumeSeconds = formList[0].consume.toLong()
                        if(consumeSeconds>60){
                            val consumeMinutes = consumeSeconds/60
                            consume_time.text = "???????????? : $consumeMinutes ???"
                        }else{
                            consume_time.text = "???????????? : $consumeSeconds ???"
                        }
                    }
                }

            }
        }




        if(car_price.text.isNullOrEmpty()){
            car_status.text="?????????"
        }
        Glide.with(MyApplication.context).load(R.drawable.ic_charging).into(charge_image)
        Glide.with(MyApplication.context).load(R.drawable.ic_connect).into(connect_image)
        if(charge_status.text.toString()=="???????????????"){
            Glide.with(MyApplication.context).load(R.drawable.chargestop).into(car_charge)
        }else{
            Glide.with(MyApplication.context).load(R.drawable.carcharge).into(car_charge)
        }

        saoyisao.setOnClickListener {

            PermissionX.init(mycontext)
                .permissions(android.Manifest.permission.CAMERA)
                .request{ allgranted,grantedlist,deniedlist->
                    if (!allgranted){
                        Toast.makeText(MyApplication.context,"????????????????????????", Toast.LENGTH_SHORT).show()
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
                    if(car_status.text.toString() == "?????????"){
                        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val time = LocalDateTime.now()
                        //???????????? ????????????
                        start_time.text = df.format(time)
                        charge_status.text = "???????????????"
                        consume_time.text = "?????????"
                        status_forms.text = "?????????"

                        TipDialog.show("????????????", WaitDialog.TYPE.SUCCESS)
                        Glide.with(MyApplication.context).load(R.drawable.carcharge).into(car_charge)
                    }else{
                        charge_status.text="???????????????"
                        //??????????????????
                        charge_switch_button.post { charge_switch_button.toggle() }
                        //????????????
                        TipDialog.show("??????????????????", WaitDialog.TYPE.WARNING)
                    }
                }
                false ->{
                    if(charge_status.text  == "???????????????"){
                        val df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        val time2 = LocalDateTime.now()
                        val time1 = LocalDateTime.parse(start_time.text.toString(),df)
                        val duration = Duration.between(time1,time2)
                        val consume = duration.toMillis()
                        val consumeSeconds = consume/1000
                        if(consumeSeconds>60){
                            val consumeMinutes = consumeSeconds/60
                            consume_time.text = "???????????? : $consumeMinutes ???"
                        }else{
                            consume_time.text = "???????????? : $consumeSeconds ???"
                        }
                        status_forms.text = "?????????"
                        val token =sp.getString("TOKEN","weifangzhou")
                        lifecycleScope.launch {
                            ChargeNetwork.postForms(token.toString(),start_time.text.toString(),consumeSeconds.toString())
                        }
                    }
                    charge_status.text="???????????????"
                    Glide.with(MyApplication.context).load(R.drawable.chargestop).into(car_charge)
                }
            }
        }

        connect_switch_button.setOnCheckedChangeListener{view,isChecked ->
            when(isChecked){
                true ->{
                    if(car_status.text.toString() == "?????????"){
                        connect_status.text = "??????????????????"
                        TipDialog.show("??????????????????", WaitDialog.TYPE.SUCCESS)
                    }else{
                        connect_status.text = "??????????????????"
                        connect_switch_button.post { connect_switch_button.toggle() }
                        TipDialog.show("??????????????????",WaitDialog.TYPE.ERROR)
                    }
                }
                false ->{
                    if(charge_status.text.toString()=="???????????????"){
                        charge_switch_button.toggle()
                        charge_status.text = "???????????????"
                    }
                    connect_status.text = "??????????????????"
                    car_status.text="?????????"
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
//                val hmsScan: HmsScan? = data?.getParcelableExtra(ScanUtil.RESULT) // ?????????????????? ScanUtil.RESULT
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