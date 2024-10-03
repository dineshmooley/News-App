package com.example.assignment11.network

import WeatherResponse
import com.example.assignment11.util.Constants.Companion.LOCATION_BASE_URL
import com.example.assignment11.util.Constants.Companion.WEATHER_API_KEY
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Weather {
    @GET("weather?")
    fun getLocation(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String = WEATHER_API_KEY
    ): Deferred<WeatherResponse>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(LOCATION_BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

object WeatherNetwork   {
    val location: Weather by lazy {
        retrofit.create(Weather::class.java)
    }
}
