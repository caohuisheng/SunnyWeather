package com.example.sunnyweather.ui

import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.forecast_item.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.now.tv_sky
import kotlinx.android.synthetic.main.now.tv_temp
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity:AppCompatActivity() {

    companion object{
        val Extra_Location_Lng = "location_lng"
        val Extra_Location_Lat = "location_lat"
        val Extra_Location_Placename = "place_name"
    }

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beautify()
        setContentView(R.layout.activity_weather)
        //从intent中取出经纬度和地区名称
        if(viewModel.locationLng.isEmpty()){
            viewModel.locationLng = intent.getStringExtra(Extra_Location_Lng) ?: ""
        }
        if(viewModel.locationLat.isEmpty()){
            viewModel.locationLat = intent.getStringExtra(Extra_Location_Lat) ?: ""
        }
        if(viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra(Extra_Location_Placename) ?: ""
        }
        //对weatherLivaData进行观察
        viewModel.weatherLivaData.observe(this, Observer{ result ->
            val weather = result.getOrNull();
            if(weather != null){
                try{
                    showWeatherInfo(weather)
                }catch(e:Exception){
                    e.printStackTrace()
                }

            }else{
                Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()
            }
        })
        //执行刷新天气请求
        viewModel.refreshWeather(viewModel.locationLng,viewModel.locationLat)
    }

    private fun beautify(){
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
    }

    /*
    现实天气信息
     */
    private fun showWeatherInfo(weather:Weather){
        tv_place.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        //填充now.xml数据
        val temp = "${realtime.temperature.toInt()} °C"
        tv_temp.text = temp
        tv_sky.text = getSky(realtime.skycon).info
        val pm25 = "空气指数 ${realtime.airQuality.aqi.chn}"
        tv_aqi.text = pm25
        layout_now.setBackgroundResource(getSky(realtime.skycon).bg)

        //填充forecast.xml中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for(i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                forecastLayout,false)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val dateTextView = view.findViewById<TextView>(R.id.tv_date)
            val skyImageView = view.findViewById<ImageView>(R.id.iv_sky)
            val skyTextView = view.findViewById<TextView>(R.id.tv_sky)
            val tempTextView = view.findViewById<TextView>(R.id.tv_temp)
            dateTextView.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyImageView.setImageResource(sky.icon)
            skyTextView.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"
            //tempTextView.text = tempText
            forecastLayout.addView(view)
        }
        //填充life_index中的数据
        val lifeIndex = daily.lifeIndex

        tv_coldRisk.text = lifeIndex.coldRisk[0].desc
        tv_dressing.text = lifeIndex.dressing[0].desc
        tv_ultraviolet.text = lifeIndex.ultraviolet[0].desc
        tv_carWashing.text = lifeIndex.carWashing[0].desc
        //让scrollView变为可见
        weatherLayout.visibility = View.VISIBLE
    }
}