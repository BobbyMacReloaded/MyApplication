package com.example.myapplication.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(

    val links: UserLinksDto,
    val name: String,
    @SerialName("profile_image")
    val profile_image: ProfileImageDto,
    val username: String
)
@Serializable
data class ProfileImageDto(
    val large: String,
    val medium: String,
    val small: String
)
@Serializable
data class UserLinksDto(
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
)