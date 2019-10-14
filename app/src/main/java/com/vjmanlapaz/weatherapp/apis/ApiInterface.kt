package com.vjmanlapaz.weatherapp.apis

import com.vjmanlapaz.weatherapp.Constants
import com.vjmanlapaz.weatherapp.models.Base
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("data/2.5/weather?appid=${Constants.WEATHER_API_KEY}")
    fun getWeather(@Query("lat") latitude: String, @Query("lon") longitude: String) : Call<Base>


}