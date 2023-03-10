package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    //创建一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    //创建一个WeatherService的动态代理对象
    private val weatherService = ServiceCreator.create<WeatherService>()

    //发起搜索城市数据请求
    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()

    //获取未来天气信息
    suspend fun getDailyWeather(lng:String,lat:String) =
        weatherService.getDailyWeather(lng,lat).await()

    //获取实时天气信息
    suspend fun getRealtimeWeather(lng:String,lat:String) =
        weatherService.getRealtimeWeather(lng,lat).await()


    //未Call对象添加扩展函数await
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body!=null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}