package com.smart.weatherapp.source

import com.smart.weatherapp.api.WeatherApi
import com.smart.weatherapp.model.WeatherDataList

class DataRepository(private val weatherApi: WeatherApi, private val lat : String, private val long: String) {

    suspend  fun getWeatherDetails() : WeatherDataList{
        return  weatherApi.getWeatherData(lat, long)
    }
}