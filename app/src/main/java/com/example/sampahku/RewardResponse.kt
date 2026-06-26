package com.example.sampahku

import com.google.gson.annotations.SerializedName

class RewardResponse {
    @SerializedName("nama_reward")
    val namaReward: String? = null

    @SerializedName("deskripsi")
    val deskripsi: String? = null

    @SerializedName("poin_dibutuhkan")
    val poinDibutuhkan: Int = 0

    @SerializedName("logo_resource_name")
    val logoResourceName: String? = null
}