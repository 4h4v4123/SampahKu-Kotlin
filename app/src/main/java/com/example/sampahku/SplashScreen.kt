package com.example.sampahku

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //untuk pindah ke MainActivity habis splash screen selesai
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val intent = Intent(this@SplashScreen, LoginActivity::class.java)
                // agar setelah splashscreen menuju halaman login
                startActivity(intent)
                finish()
            }
        }, SPLASH_SCREEN_DURATION.toLong())
    }

    companion object {
        //durasi splash screen = 3 detik (3000 milidetik)
        private const val SPLASH_SCREEN_DURATION = 3000
    }
}