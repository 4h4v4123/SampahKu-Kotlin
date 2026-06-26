package com.example.sampahku

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("status")
    val status: String? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("user_id")
    val userId: Int = 0

    @SerializedName("nama")
    val nama: String? = null
}