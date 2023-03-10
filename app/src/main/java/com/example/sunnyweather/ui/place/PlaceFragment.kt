package com.example.sunnyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.MainActivity
import com.example.sunnyweather.R
import com.example.sunnyweather.ui.WeatherActivity
import com.google.android.material.stateful.ExtendableSavedState
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment:Fragment() {

    //使用懒加载计数获取PlaceViewModel实例
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }

    private lateinit var adapter:PlaceAdapter

    override fun onCreateView(inflater:LayoutInflater,container:ViewGroup?,
                              savedState: Bundle?):View{
        val view =  inflater.inflate(R.layout.fragment_place,container,false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restorePlace()
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        recyclerView.adapter = adapter
        //监听搜索框内容的变化
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if(content.isNotEmpty()){
                //获取新的内容
                viewModel.searchPlaces(content)
            }else{
                //搜索框内容为空时仅显示背景图片
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        //观察placeViewModel中的placeLiveData对象
        viewModel.placeLiveData.observe(this,Observer{result ->
            val places = result.getOrNull()
            //如果数据不为空
            if(places != null){
                //显示城市数据
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT)
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    /*
    恢复城市信息
     */
    private fun restorePlace(){
        if(viewModel.isPlaceSaved()){
            val place = viewModel.getSavedPlace()
            val intent = Intent(context,WeatherActivity::class.java).apply{
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
        }
    }
}