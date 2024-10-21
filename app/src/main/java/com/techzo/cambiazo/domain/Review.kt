package com.techzo.cambiazo.domain

data class Review (
    val message: String,
    val rating: Int,
    val state: String,
    val exchangeId: Int,
    val userAuthorId: Int,
    val userReceptorId: Int
)

data class ReviewAverageUser (
    val averageRating: Double,
    val countReviews: Int,
)

data class ReviewWithAuthorDetails(
    val message: String,
    val rating: Int,
    val state: String,
    val exchangeId: Int,
    val userAuthorId: Int,
    val userReceptorId: Int,
    val userAuthor: User
)