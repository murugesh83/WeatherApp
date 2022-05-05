package com.smart.weatherapp.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smart.weatherapp.model.WeatherDailyData
import com.smart.weatherapp.model.WeatherData
import com.smart.weatherapp.model.WeatherDataList
import com.smart.weatherapp.source.DataRepository
import com.smart.weatherapp.source.ManualParsingAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewWeatherListModel(latitude : String, private val longtude : String) : ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherData>()
    val weatherLiveDailyData = MutableLiveData<List<List<WeatherDailyData>>>()
    private val dataRepository : DataRepository
    init {
        dataRepository = DataRepository(ManualParsingAPI(), latitude, longtude)
        Log.d("MainViewWeatherListMode", "muru $latitude" )
        Log.d("MainViewWeatherListMode", "muru $longtude" )

    }



    fun fetchData() {
        viewModelScope.launch {
          val weather :WeatherData = withContext(Dispatchers.IO){
            dataRepository.getWeatherDetails().weather[0]
          }
            Log.d("MainViewWeatherListMode", "muru $weather" )
            weatherLiveData.value = weather

            val weatherDaily :List<List<WeatherDailyData>> = withContext(Dispatchers.IO){
                dataRepository.getWeatherDetails().weatehrDailyValue
            }

            weatherLiveDailyData.value = weatherDaily;
            Log.d("WeahterdataListMode1*1", "muru $weatherDaily" )

        }
    }
}