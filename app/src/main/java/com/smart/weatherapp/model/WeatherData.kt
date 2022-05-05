package com.smart.weatherapp.model

import java.util.function.DoubleBinaryOperator

data class WeatherDataList(
    val weather :List<WeatherData>,
    val weatehrDailyValue : List<List<WeatherDailyData>>
)
data class WeatherData(
    val id:Int,
    val main:String,
    val description:String,
    val icon:String,

    val speed:Double,
    val deg:Double,

    val country:String,
    val sunrise:Long,
    val sunset:Long,

    val name:String
)

data class WeatherDailyData(
    val dt: Long,
    val sunrise : Long,
    val sunset : Long,
    val moonrise : Long,
    val moonset :Long,

    val day : Double,
    val min : Double,
    val max : Double,
    val night : Double,
    val eve : Double,
    val morn : Double,

    val wind_speed : Double,

    val pressure : Long,
    val humidity : Long,


    val main : String,
    val description : String
)


/*
{"coord":{"lon":-75.2769,"lat":40.2394},
    "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],
    "base":"stations",
    "main":{"temp":291.4,"feels_like":291.34, "temp_min":289.55,"temp_max":292.98,"pressure":1013,"humidity":79},
    "visibility":10000,
    "wind":{"speed":2.06,"deg":220},
    "clouds":{"all":40},
    "dt":1651699124,
    "sys":{"type":2,"id":2004588,"country":"US","sunrise":1651658216,"sunset":1651708724},
    "timezone":-14400,
    "id":5197159,
    "name":"Lansdale",
    "cod":200}


    "daily":[{"dt":1652284800,"sunrise":1652262554,"sunset":1652313951,"moonrise":1652295900,"moonset":1652254680,
    "moon_phase":0.34,"temp":{"day":287.51,"min":285.26,"max":291.54,"night":289.21,"eve":291.54,"morn":285.26},
    "feels_like":{"day":287.35,"night":289.33,"eve":291.55,"morn":284.88},"pressure":1023,"humidity":90,
    "dew_point":285.86,"wind_speed":4.46,"wind_deg":62,"wind_gust":11.4,
    "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
    "clouds":100,"pop":0,"uvi":1}]

*/

