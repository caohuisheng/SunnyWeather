package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

//地点返回数据
data class PlaceResponse(val status:String,val places:List<Place>)

//地点
data class Place(val name:String,val location:Location,
                 @SerializedName("formatted_address")val address:String)

//地点位置（经度，纬度）
data class Location(val lng:String,val lat:String)
