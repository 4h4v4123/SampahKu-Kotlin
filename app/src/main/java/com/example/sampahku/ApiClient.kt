package com.example.sampahku

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Ingat, 10.0.2.2 adalah localhost untuk Android Emulator!
    private const val BASE_URL = "http://10.0.2.2:8000/"
    private var retrofit: Retrofit? = null

    @JvmStatic
    val service: ApiService
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create<ApiService>(ApiService::class.java)
        }
}