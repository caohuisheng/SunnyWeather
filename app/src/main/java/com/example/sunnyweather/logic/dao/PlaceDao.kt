package com.example.sunnyweather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.Place
import com.google.gson.Gson

object PlaceDao {
    /*
    保存数据
     */
    fun savePlace(place:Place){
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    /*
    恢复数据
     */
    fun getSavePlace():Place{
        val placeJson = sharedPreferences().getString("place","")
        return Gson().fromJson(placeJson,Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context
        .getSharedPreferences(SunnyWeatherApplication.KEY, Context.MODE_PRIVATE)
}