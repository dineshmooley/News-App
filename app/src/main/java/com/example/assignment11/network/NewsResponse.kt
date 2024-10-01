package com.example.assignment11.network

import com.example.assignment11.domain.Articles

data class NewsResponse (
    val articles: List<Articles>,
    val status: String,
    val totalResults: Int
)