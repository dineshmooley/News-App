package com.example.assignment11.repository

import WeatherResponse
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.example.assignment11.network.WeatherNetwork
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepository(context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(): Task<Location> {
        return fusedLocationClient.lastLocation
    }

    suspend fun getWeatherData(lat: String, lon: String): WeatherResponse? {
        var weatherData: WeatherResponse?

        withContext(Dispatchers.IO) {
            weatherData = WeatherNetwork.location.getLocation(lat, lon).await()
        }
        return weatherData
    }
}