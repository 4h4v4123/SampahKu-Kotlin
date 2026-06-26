package com.example.sampahku

import com.google.gson.annotations.SerializedName

class RiwayatResponse {
    @JvmField
    @SerializedName("nama_sampah")
    val namaSampah: String? = null

    @JvmField
    @SerializedName("berat")
    val berat: Double = 0.0

    @JvmField
    @SerializedName("poin_didapat")
    val poinDidapat: Int = 0

    @JvmField
    @SerializedName("tanggal_lokasi")
    val tanggalLokasi: String? = null
}