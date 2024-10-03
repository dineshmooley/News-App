package com.example.assignment11.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.assignment11.database.ArticleDatabase
import com.example.assignment11.database.asDomainModel
import com.example.assignment11.domain.Articles
import com.example.assignment11.network.Network
import com.example.assignment11.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NewsRepository(private val database: ArticleDatabase) {

    val news: LiveData<List<Articles>> = database.getArticleDao.getAllArticles().map {
        it.asDomainModel()
    }

    suspend fun refreshNews() {
        withContext(Dispatchers.IO) {
            val articleList = Network.news.getNews().await().articles
            try {
                database.getArticleDao.insert(*articleList.asDomainModel())
            } catch (e: Exception) {
                println("Server Error: $e")
            }
        }
    }
}