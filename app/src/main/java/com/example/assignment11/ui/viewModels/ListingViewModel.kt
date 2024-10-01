package com.example.assignment11.ui.viewModels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.assignment11.database.getDatabase
import com.example.assignment11.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ListingViewModel(application: Application) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val newsRepository = NewsRepository(database)

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