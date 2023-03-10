package com.example.sunnyweather.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Place

class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    //将searchLiveData对象转换为placeLiveData对象
    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query:String){
        searchLiveData.value = query
    }

    fun savePlace(place:Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}