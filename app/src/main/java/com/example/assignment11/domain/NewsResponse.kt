package com.example.assignment11.domain

data class NewsResponse (
    val articles: List<Articles>,
    val status: String,
    val totalResults: Int
)