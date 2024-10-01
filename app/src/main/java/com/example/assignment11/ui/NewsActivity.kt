package com.example.assignment11.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.assignment11.R
import com.example.assignment11.database.ArticleDatabase
import com.example.assignment11.repository.NewsRepository
import com.example.assignment11.ui.viewModels.ListingViewModel

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

}