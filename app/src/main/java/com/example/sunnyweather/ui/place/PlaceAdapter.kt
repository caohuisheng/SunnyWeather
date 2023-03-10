package com.example.sunnyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.example.sunnyweather.logic.model.Place
import com.example.sunnyweather.ui.WeatherActivity

class PlaceAdapter(private val fragment:PlaceFragment,private val placeList:List<Place>)
    :RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {

    inner class PlaceHolder(view: View):RecyclerView.ViewHolder(view){
        val placeName:TextView = view.findViewById(R.id.placeName)
        val placeAddress:TextView = view.findViewById(R.id.placeAddress)
    }

    //创建ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        val holder = PlaceHolder(view)
        holder.itemView.setOnClickListener{
            val positon = holder.adapterPosition
            val place = placeList[positon]
            val intent = Intent(parent.context,WeatherActivity::class.java).apply{
                putExtra(WeatherActivity.Extra_Location_Lng,place.location.lng)
                putExtra(WeatherActivity.Extra_Location_Lat,place.location.lat)
                putExtra(WeatherActivity.Extra_Location_Placename,place.name)
            }
            //保存城市数据
            fragment.viewModel.savePlace(place)
            fragment.startActivity(intent)
            fragment.activity?.finish()
        }
        return holder
    }

    //绑定ViewHolder
    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
        //holder.itemView.setOnClickListener()
    }

    override fun getItemCount(): Int {
        return placeList.size
    }
}