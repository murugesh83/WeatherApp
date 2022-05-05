package com.smart.weatherapp.api

import com.smart.weatherapp.model.WeatherDataList

interface WeatherApi {
   suspend fun getWeatherData(lat :String, long :String) : WeatherDataList
}