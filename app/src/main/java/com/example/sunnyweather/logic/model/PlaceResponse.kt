package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * {
"status": "ok",
"query": "北京",
"places": [
{
"id": "B000A83AJN",
"name": "北京南站",
"formatted_address": "中国 北京市 丰台区 永外大街车站路12号",
"location": {
"lat": 39.865246,
"lng": 116.378517
},
"place_id": "a-B000A83AJN"
}
 */

//地点返回数据
data class PlaceResponse(val status:String,val places:List<Place>)

//地点
data class Place(val name:String,val location:Location,
                 @SerializedName("formatted_address")val address:String)

//地点位置（经度，纬度）
data class Location(val lng:String,val lat:String)
