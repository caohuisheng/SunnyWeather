package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

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

    /*
    刷新天气信息
     */
//    fun refreshWeather(lng:String,lat:String) = liveData(Dispatchers.IO){
//        val result = try{
//            //创建一个协程作用域
//            coroutineScope {
//                val deferredRealtime = async{
//                    SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
//                }
//                val deferredDaily = async{
//                    SunnyWeatherNetwork.getDailyWeather(lng,lat)
//                }
//                val realtimeResponse = deferredRealtime.await()
//                val dailyResponse = deferredDaily.await()
//                if(realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
//                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
//                    Result.success(weather)
//                }else{
//                    Result.failure(
//                        RuntimeException(
//                            "realtime response status is ${realtimeResponse.status}" +
//                                    "daily response status is ${dailyResponse.status}"
//                        )
//                    )
//                }
//            }
//
//        }catch(e:Exception){
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }

    fun refreshWeather(lng:String,lat:String) = fire<Weather>(Dispatchers.IO){
        coroutineScope {
            val deferredRealtime = async{
                SunnyWeatherNetwork.getRealtimeWeather(lng,lat)
            }
            val deferredDaily = async{
                SunnyWeatherNetwork.getDailyWeather(lng,lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if(realtimeResponse.status == "ok" && dailyResponse.status == "ok"){
                val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context:CoroutineContext,block:suspend() -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try{
                //调用传入的lambda表达式
                block()
            }catch(e:Exception){
                Result.failure<T>(e)
            }
            //将lambda表达式的执行结果发射出去
            emit(result)
        }

    fun savePlace(place:Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavePlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}