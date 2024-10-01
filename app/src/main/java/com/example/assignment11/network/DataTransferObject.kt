package com.example.assignment11.network

import com.example.assignment11.database.DatabaseArticles
import com.example.assignment11.domain.Articles

fun List<Articles>.asDomainModel(): Array<DatabaseArticles> {
    return map {
        DatabaseArticles(
            author = it.author,
            content = it.content,
            description = it.description,
            publishedAt = it.publishedAt,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }.toTypedArray()
}