package com.example.bikestockproject.modeldata


import com.google.gson.annotations.SerializedName

/**
 * Model untuk Login Request
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String
)

/**
 * Model untuk User
 */
data class UserModel(
    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("username")
    val username: String,

    @SerializedName("role")
    val role: String,

    @SerializedName("created_at")
    val createdAt: String? = null
)

/**
 * Model untuk Login Response Data
 */
data class LoginData(
    @SerializedName("token")
    val token: String
)

/**
 * Model untuk Login Response
 */
data class LoginResponse(
    @SerializedName("status")
    val status: String,  // "success" atau "error"

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: LoginData? = null
)

/**
 * Response umum untuk operasi yang tidak mengembalikan data
 */
data class BaseResponse(
    @SerializedName("status")
    val status: String,  // "success" atau "error"

    @SerializedName("message")
    val message: String
)