package com.example.assignment11.ui.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.assignment11.database.getDatabase
import com.example.assignment11.domain.Articles
import com.example.assignment11.repository.LocationRepository
import com.example.assignment11.repository.NewsRepository
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ListingViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

    private val _navigateToArticle = MutableLiveData<String?>()
    val navigateToArticle: LiveData<String?>
        get() = _navigateToArticle

    private val _location = MutableLiveData<Location?>()
    val location: LiveData<Location?>
        get() = _location

    private val _weather = MutableLiveData<String>()
    val weather: LiveData<String>
        get() = _weather

    private val _locationName = MutableLiveData<String>()
    val locationName: LiveData<String>
        get() = _locationName

    private val _iconCode = MutableLiveData<String>()
    val iconCode: LiveData<String>
        get() = _iconCode

    private val _temp = MutableLiveData<String?>()
    val temp: LiveData<String?>
        get() = _temp

    val searchQuery = MutableLiveData<String?>()

    private val locationRepository = LocationRepository(application.applicationContext)

    fun fetchLocation() {
        locationRepository.getCurrentLocation().addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                _location.value = task.result
            } else {
                _location.value = null
            }
        })
    }

    fun onSearchQueryChanged(query: String): List<Articles>? {
        return newsList.value?.filter {
            it.title.lowercase().contains(query.lowercase().trim())
        }
    }

    @SuppressLint("DefaultLocale")
    fun getWeatherData(lat: String, lon: String) {
        viewModelScope.launch {
            try {
                // Fetch weather data from the repository
                val dataArray = locationRepository.getWeatherData(lat, lon)

                // If data is not null, update the LiveData values
                dataArray?.let { data ->
                    _weather.value = data.weather[0].description
                    _locationName.value = data.name
                    _iconCode.value = data.weather[0].icon
                    _temp.value = String.format("%.1f°", (data.main.temp - 273.15))  // Kelvin to Celsius
                } ?: run {
                    // Handle the case when dataArray is null (if your API fails to return data)
                    _weather.value = "Unknown"
                    _locationName.value = "Unknown Location"
                    _temp.value = "--"
                }

            } catch (e: Exception) {
                // Handle any exceptions (e.g., network errors)
                _weather.value = "Error"
                _locationName.value = "Error retrieving location"
                _temp.value = "--"
            }
        }
    }



    fun onArticleClicked(id: String) {
        _navigateToArticle.value = id
    }

    fun onNavigatedToArticle() {
        _navigateToArticle.value = null
    }

    init {
        viewModelScope.launch {
            newsRepository.refreshNews()
        }
    }

    val newsList = newsRepository.news

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}