package com.example.mycharge.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.services.core.ServiceSettings
import com.example.mycharge.R
import com.example.mycharge.network.ChargeNetwork
import com.kongzue.dialogx.dialogs.FullScreenDialog
import com.kongzue.dialogx.interfaces.OnBindView
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var myMapView: MapView

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

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         myMapView = view.findViewById(R.id.mapView)
        ServiceSettings.updatePrivacyShow(context,true,true)
        ServiceSettings.updatePrivacyAgree(context,true)
        myMapView.onCreate(savedInstanceState)
        val map = myMapView.map
        //显示定位小蓝点
        MyLocationStyle().apply {
            interval(2000)
            showMyLocation(true)
            map.myLocationStyle = this
            map.isMyLocationEnabled = true
        }

        map.moveCamera(CameraUpdateFactory.zoomTo(17F))
        //初始化充电桩位置信息
        lifecycleScope.launch {
            val places = ChargeNetwork.serachCharges().places
            for( place in places){
                map.addMarker(
                    MarkerOptions().apply {
                        position(LatLng(place.latitude.toDouble(),place.longitude.toDouble()))
                        title("充电桩").snippet(place.status)
                        draggable(false)
                        isFlat = true
                    }
                )
            }
        //处理充电桩点击事件
            map.setOnMarkerClickListener {
                for (place in places){
                    if (it.position.latitude.toString() == place.latitude &&it.position.longitude.toString()==place.longitude){
                        FullScreenDialog.show(object :OnBindView<FullScreenDialog>(R.layout.message){
                            @SuppressLint("SetTextI18n")
                            override fun onBind(dialog: FullScreenDialog, v: View) {
                                val chargePrice = v.findViewById<TextView>(R.id.charge_price)
                                val chargePower = v.findViewById<TextView>(R.id.charge_power)
                                val chargeStatus = v.findViewById<TextView>(R.id.charge_status)
                                chargePower.text ="功率:  ${place.maxpower}"
                                chargePrice.text ="价格:  ${place.price}"
                                chargeStatus.text="状态:  ${place.status}"
                                dialog.isHideZoomBackground=true
                            }
                        })
                    }
                }
                it.showInfoWindow()
                true
            }



        }





    }

    override fun onDestroy() {
        super.onDestroy()
        myMapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        myMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        myMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        myMapView.onSaveInstanceState(outState)
    }









    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}