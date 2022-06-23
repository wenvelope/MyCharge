package com.example.mycharge.network

data class ChargePlace(val places:List<Place>)
data class Place(val _id:String,val latitude:String,val longitude:String,val status:String,val maxpower:String,val price:String)
