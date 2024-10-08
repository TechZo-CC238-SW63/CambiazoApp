package com.techzo.cambiazo.data.remote

import com.google.gson.annotations.SerializedName
import com.techzo.cambiazo.domain.model.User

data class UserResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("token")
    val token: String
)

fun UserResponseDto.toUser() = User(
    username = username,
    token = token
)