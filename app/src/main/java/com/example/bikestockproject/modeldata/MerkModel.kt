package com.example.bikestockproject.modeldata


import com.google.gson.annotations.SerializedName

/**
 * Model data untuk Merk Sepeda
 */
data class MerkModel(
    @SerializedName("merk_id")
    val merkId: Int? = null,

    @SerializedName("nama_merk")
    val namaMerk: String,

    @SerializedName("created_at")
    val createdAt: String? = null
)

/**
 * Response untuk list merk
 */
data class MerkListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: List<MerkModel>? = null
)

/**
 * Response untuk single merk
 */
data class MerkResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: MerkModel? = null
)