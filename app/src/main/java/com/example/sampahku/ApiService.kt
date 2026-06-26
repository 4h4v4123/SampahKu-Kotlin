package com.example.sampahku

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("api/login/")
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse?>?

    // Mendaftarkan user baru (Register)
    @FormUrlEncoded
    @POST("api/pengguna/")
    fun registerUser(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("no_telepon") noTelepon: String?,
        @Field("alamat") alamat: String?,
        @Field("total_poin") totalPoin: Int
    ): Call<ProfilResponse?>?

    @GET("api/pengguna/{id}/")
    fun getProfil(@Path("id") id: Int): Call<ProfilResponse?>?

    // Menghapus akun secara permanen
    @DELETE("api/pengguna/{id}/")
    fun hapusProfil(@Path("id") id: Int): Call<Void?>?

    @get:GET("api/riwayat/")
    val riwayat: Call<MutableList<RiwayatResponse?>?>?

    @get:GET("api/edukasi/")
    val edukasi: Call<MutableList<EdukasiResponse?>?>?

    @get:GET("api/reward/")
    val reward: Call<MutableList<RewardResponse?>?>?

    // Mengubah data profil secara spesifik menggunakan PATCH
    @FormUrlEncoded
    @PATCH("api/pengguna/{id}/")
    fun updateProfil(
        @Path("id") id: Int,  // ID pengguna yang ingin diubah
        @Field("nama") nama: String?,
        @Field("no_telepon") noTelepon: String?,
        @Field("alamat") alamat: String?
    ): Call<ProfilResponse?>?
}