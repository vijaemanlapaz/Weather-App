package com.vjmanlapaz.weatherapp.apis

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.vjmanlapaz.weatherapp.BuildConfig
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private const val API_BASE_URL = "https://api.openweathermap.org/"

        fun create(): ApiInterface {

            val retrofit = Retrofit.Builder().baseUrl(API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                        .setLenient()
                        .create()))
                    .client(OkHttpClient.Builder().apply {
                        this.addInterceptor(HeaderInterceptor().apply {
                            this.isDebug = BuildConfig.DEBUG
                        })
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60 / 2, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS)
//                            .cache(null)
                    }.build())
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}
