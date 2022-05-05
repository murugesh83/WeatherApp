package com.smart.weatherapp.source

import android.util.Log
import com.smart.weatherapp.api.WeatherApi
import com.smart.weatherapp.model.WeatherDailyData
import com.smart.weatherapp.model.WeatherData
import com.smart.weatherapp.model.WeatherDataList
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.net.URL
import java.net.URLConnection

class ManualParsingAPI : WeatherApi {

    override suspend fun getWeatherData(latitude: String, longtiude : String): WeatherDataList {

        val mLatitude : Double= latitude.toDouble()
        val mLongtidue : Double = longtiude.toDouble()
        val url = URL("https://api.openweathermap.org/data/2.5/weather?lat=$mLatitude&lon=$mLongtidue&appid=abcfac0bf2d376175662be64fd4ec220")
        val connection = url.openConnection()
        connection.connect()

        val bufferedInputStream =BufferedInputStream(connection.getInputStream())
        val bufferedReader : BufferedReader = bufferedInputStream.bufferedReader()

        val stringBuffer =StringBuffer()

        for (line in bufferedReader.readLine()){
            stringBuffer.append(line)
        }

        bufferedReader.close()

        val fulljson = stringBuffer.toString()

        print("muru data : $fulljson")
        Log.d("ManualParasing ", "muru data : $fulljson")

        val jsonObjectData = JSONObject(fulljson)

        val jsonArrayWeather : JSONArray = jsonObjectData.getJSONArray("weather")
        val jsonObjectWeather = jsonArrayWeather.getJSONObject(0)
        val weatherId : Int = jsonObjectWeather.getInt("id")
        val weatherMain : String = jsonObjectWeather.getString("main")
        val weatherDescription : String = jsonObjectWeather.getString("description")
        val weatherIcon : String = jsonObjectWeather.getString("icon")
        Log.d("ManualParasing ", "muru Weather data : $weatherId $weatherMain $weatherDescription $weatherIcon")


        val jsonArrayWind : JSONObject = jsonObjectData.getJSONObject("wind")
        val windSpeed = jsonArrayWind.getDouble("speed");
        val wingdeg = jsonArrayWind.getDouble("deg")

       // Log.d("ManualParasing ", "muru jsonArrayWind Weather data : $windSpeed $wingdeg")

        val jsonArraySys : JSONObject = jsonObjectData.getJSONObject("sys")
        val sysCountry = jsonArraySys.getString("country");
        val sysSunrise = jsonArraySys.getLong("sunrise")
        val sysSunset = jsonArraySys.getLong("sunset")

        //Log.d("ManualParasing ", "muru jsonArraySys weather data : $sysCountry $sysSunrise $sysSunset ")
        val jsonArrayName : String = jsonObjectData.optString("name")
        //Log.d("ManualParasing ", "muru jsonArrayName weather data : $jsonArrayName")

        val url1 = URL("https://api.openweathermap.org/data/2.5/onecall?lat=$mLatitude&lon=$mLongtidue&exclude=hourly&appid=abcfac0bf2d376175662be64fd4ec220")

        val connection1= url1.openConnection()

        connection1.connect()

        val bufferedInputStream1=BufferedInputStream(connection1.getInputStream())
        val bufferedReader1 : BufferedReader = bufferedInputStream1.bufferedReader()

        val stringBuffer1 =StringBuffer()

        for (line in bufferedReader1.readLine()){
            stringBuffer1.append(line)
        }

        bufferedReader1.close()

        val fulljson2 = stringBuffer1.toString()

        print("muru data : $fulljson2")
        //Log.d("ManualParasing ", "muru data : $fulljson2")

        val jsonObjectData1 = JSONObject(fulljson2)

        val jsonArrayDaily1 : JSONArray = jsonObjectData1.getJSONArray("daily")
        var weatherListData = mutableListOf<List<WeatherDailyData>>()
        for (i in 0 until jsonArrayDaily1.length()){
            val jsonObjectWeatherDaily = jsonArrayDaily1.getJSONObject(i)
            val dt1 : Long = jsonObjectWeatherDaily.getLong("dt")
            val weatherDailysunrise : Long = jsonObjectWeatherDaily.getLong("sunrise")
            val weatherDailysunset : Long = jsonObjectWeatherDaily.getLong("sunset")
            val weatherDailymoonrise : Long = jsonObjectWeatherDaily.getLong("moonrise")
            val weatherDailymoonset : Long = jsonObjectWeatherDaily.getLong("moonset")


            val weatherDailytemp : JSONObject = jsonObjectWeatherDaily.getJSONObject("temp")
            val weatherDailytempday = weatherDailytemp.getDouble("day");
            val weatherDailytempmin = weatherDailytemp.getDouble("min");
            val weatherDailytempmax = weatherDailytemp.getDouble("max");
            val weatherDailytempnight = weatherDailytemp.getDouble("night");
            val weatherDailytempeve = weatherDailytemp.getDouble("eve");
            val weatherDailytempmorn = weatherDailytemp.getDouble("morn");

            val weatherDailywindspeed1 : Double = jsonObjectWeatherDaily.getDouble("wind_speed")

            val weatherDailywindpressure : Long = jsonObjectWeatherDaily.getLong("pressure")
            val weatherDailywindhumidity : Long = jsonObjectWeatherDaily.getLong("humidity")


            val jsonArrayWeatherDaily : JSONArray = jsonObjectWeatherDaily.getJSONArray("weather")
            val jsonObjectWeatherDaily1 : JSONObject = jsonArrayWeatherDaily.getJSONObject(0)

            val weatherDailyMain : String = jsonObjectWeatherDaily1.getString("main")
            val weatherDailyDescription : String = jsonObjectWeatherDaily1.getString("description")

            val list = listOf(WeatherDailyData(dt = dt1,
            sunrise = weatherDailysunrise,
            sunset = weatherDailysunset,
            moonrise = weatherDailymoonrise,
            moonset =  weatherDailymoonset,
            day = weatherDailytempday,
            min =weatherDailytempmin,
            max = weatherDailytempmax,
            night = weatherDailytempnight,
            eve = weatherDailytempeve,
            morn = weatherDailytempmorn,
            wind_speed = weatherDailywindspeed1,
            pressure = weatherDailywindpressure,
            humidity = weatherDailywindhumidity,
            main = weatherDailyMain,
            description = weatherDailyDescription))
                 weatherListData.add(list)
/*
            Log.d("ManualParasing ", "muru Weather data : $dt1" +
                    " $weatherDailysunrise" +
                    " $weatherDailysunset" +
                    " $weatherDailymoonrise"+
                    " $weatherDailymoonset" +
                    " $weatherDescription" +
                    " $weatherDailytempday" +
                    " $weatherDescription" +
                    " $weatherDailytempmin"+
                    " $weatherDailytempmax" +
                    " $weatherDailytempnight" +
                    " $weatherDailytempeve" +
                    " $weatherDailytempmorn"+
                    " $weatherDailywindspeed1" +
                    " $weatherDailywindpressure"+
                    " $weatherDailywindhumidity" +
                    " $weatherDailyMain" +
                    " $weatherDailyDescription")*/

        }

        return WeatherDataList(listOf(WeatherData(id= weatherId,main = weatherMain, description = weatherDescription,icon = weatherIcon,
        speed = windSpeed, deg = wingdeg, country = sysCountry,
        sunrise = sysSunrise, sunset = sysSunset,
        name = jsonArrayName)), weatherListData)


    }
}