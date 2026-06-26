package com.example.sampahku

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QrActivity : AppCompatActivity(), View.OnClickListener {
    private var ivBack: ImageView? = null
    private var btnResend: LinearLayout? = null
    private var tvCountdownLabel: TextView? = null

    //konsep timer countdown untuk kode qr-nya:
    // parameter pertama adalah 90000: total durasi dalam milidetik
    /*
    * 90000 ms = 1 menit 30 detik (90 detik)
    * lalu ada parameter kedua 1000.
    * 1000 adalah interval update dalam milidetik juga.
    * Jadi setiap 1 detik sekali, onTick() itu dipanggil
    * Setiap detik, fungsi itu akan mengupdate teks countdownnya
    * di mana onFinish() akan dipanggil ketika waktu sdh habis
    *
    * */
    private var countDownTimer: CountDownTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.hide()
        }

        // inisialisasi view-nya
        ivBack = findViewById<ImageView>(R.id.iv_back)
        btnResend = findViewById<LinearLayout>(R.id.btn_resend)
        tvCountdownLabel = findViewById<TextView>(R.id.tv_countdown_label)

        ivBack!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        btnResend!!.setOnClickListener(this)

        // dipakai untuk mengaktifkan countdown
        startCountdown()
    }

    override fun onClick(v: View) {
        // if (v.getId() == R.id.iv_back) {
        //finish();

        //}

        if (v.getId() == R.id.btn_resend) {
            btnResend!!.setAlpha(0.4f)

            // ini dipakai untuk reset countdown ketika tombol resend ditekan
            if (countDownTimer != null) {
                countDownTimer!!.cancel()
            }
            startCountdown()
        }
    }

    // memulai fungsi perhitungan
    private fun startCountdown() {
        btnResend!!.setEnabled(false)
        btnResend!!.setAlpha(0.4f)

        countDownTimer = object : CountDownTimer(DURATION_MS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val totalDetik = millisUntilFinished / 1000
                val menit = totalDetik / 60
                val detik = totalDetik % 60

                val waktu = String.format("%02d:%02d", menit, detik)
                tvCountdownLabel!!.setText("Kode Akan Kadarluasa Dalam " + waktu)
            }

            override fun onFinish() {
                tvCountdownLabel!!.setText("Kode Telah Kadaluarsa")
                btnResend!!.setEnabled(true)
                btnResend!!.setAlpha(1.0f)
                btnResend!!.setBackgroundResource(R.drawable.bg_button_green)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    companion object {
        private const val DURATION_MS: Long = 90000 // 90 detik
    }
}