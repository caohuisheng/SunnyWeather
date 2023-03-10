package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication:Application() {
    companion object {
        //token
        const val TOKEN = "bLMxAPzEQcEyL2is"

        //context
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context

        //key
        const val KEY = "sunny_weather"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}