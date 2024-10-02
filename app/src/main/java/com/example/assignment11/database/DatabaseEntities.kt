package com.example.assignment11.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.assignment11.domain.Articles

@Entity(tableName = "articles")
data class DatabaseArticles(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val title: String,
    val url: String,
    val urlToImage: String?
)

fun List<DatabaseArticles>.asDomainModel(): List<Articles> {
    return map {
        Articles(
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}