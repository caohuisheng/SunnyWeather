package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {
    fun searchPlaces(query:String) = liveData(Dispatchers.IO) {
        val result = try{
            //搜索城市数据
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                //包装获取的城市数据列表
                Result.success(places)
            }else{
                //包装一个异常信息
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch(e:Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}