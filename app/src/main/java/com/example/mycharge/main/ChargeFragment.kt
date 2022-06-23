package com.example.mycharge.main

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.mycharge.HomeActivity
import com.example.mycharge.MyApplication
import com.example.mycharge.R
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.huawei.hms.ml.scan.HmsScanBase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChargeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mycontext :HomeActivity
    private lateinit var saoyisao :Button
    private lateinit var saoma:TextView

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
        return inflater.inflate(R.layout.fragment_charge, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mycontext = context as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saoma = view.findViewById(R.id.saoma)
        saoyisao = view.findViewById(R.id.saoyisao)
        saoyisao.setOnClickListener {
            val option = HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create()
            ScanUtil.startScan(
                mycontext,200,option
            )
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