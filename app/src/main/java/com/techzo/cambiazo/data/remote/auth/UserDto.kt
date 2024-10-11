package com.techzo.cambiazo.data.remote.auth

import com.google.gson.annotations.SerializedName
import com.techzo.cambiazo.domain.User

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("username")
    val username: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("profilePicture")
    val profilePicture: String,
    @SerializedName("roles")
    val roles: List<String>
)

fun UserDto.toUser() = User(
    id = id,
    username = username,
    name = name,
    phoneNumber = phoneNumber,
    profilePicture = profilePicture,
    roles = roles
)