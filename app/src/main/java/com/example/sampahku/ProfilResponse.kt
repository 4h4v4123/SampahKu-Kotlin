package com.example.sampahku

import com.google.gson.annotations.SerializedName

class ProfilResponse {
    @SerializedName("nama")
    val nama: String? = null

    @SerializedName("email")
    val email: String? = null

    @SerializedName("no_telepon")
    val noTelepon: String? = null

    @SerializedName("alamat")
    val alamat: String? = null

    @SerializedName("total_poin")
    val totalPoin: Int = 0
}