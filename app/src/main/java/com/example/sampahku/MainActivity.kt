package com.example.sampahku

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sampahku.ApiClient.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// untuk import fungsi Intent
// untuk intent yang implicit
// saya justru masih ragu ini library kebanyakan dipake atau gk ya?
// (kinda done?) FIGURE SOMETHING OUT OR SMTH IDK
class MainActivity : AppCompatActivity(), View.OnClickListener {
    //navbar yang dibuat menurut desain figma
    // memakai linearlayout
    private var navHome: LinearLayout? = null
    private var navReward: LinearLayout? = null
    private var navQr: View? = null
    private var navStatistik: LinearLayout? = null
    private var navProfil: LinearLayout? = null

    //linear layout lagi namun untuk tombol "TUkar poin"
    private var tblTukarPoin: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.hide()
        }

        //initialisasi item-item yang ada di navbar di bawah layar nanti
        // ditambah tombol tukar poinnya
        navHome = findViewById<LinearLayout>(R.id.nav_home)
        navReward = findViewById<LinearLayout>(R.id.nav_reward)
        navQr = findViewById<View>(R.id.nav_qr)
        navStatistik = findViewById<LinearLayout>(R.id.nav_statistik)
        navProfil = findViewById<LinearLayout>(R.id.nav_profil)
        val ivProfile = findViewById<ImageView>(R.id.iv_profile)

        tblTukarPoin = findViewById<LinearLayout>(R.id.btn_tukar_poin)

        //setOnClickListener
        navHome!!.setOnClickListener(this)
        navReward!!.setOnClickListener(this)
        navQr!!.setOnClickListener(this)
        navStatistik!!.setOnClickListener(this)
        navProfil!!.setOnClickListener(this)
        tblTukarPoin!!.setOnClickListener(this)
        ivProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, ProfilActivity::class.java))
            }
        })

        //set data-data utk item aktivitas
        // karena memakai <include> maka saya perlu set
        // secara manual
        setupAktivitasItems()
        setupEdukasiItems()
        setActiveNav()
    }

    override fun onClick(v: View) {
        if (v.getId() == R.id.nav_home) {
            // Ini hanya memastikan apakah tombol home bekerja
            // aslinya sdh di home
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
            // menggunakan inten untuk menuju ke halaman Reward
        } else if (v.getId() == R.id.nav_reward) {
            startActivity(Intent(this@MainActivity, RewardActivity::class.java))

            //menggunakan inten untuk menuju ke halaman scan QR
        } else if (v.getId() == R.id.nav_qr) {
            startActivity(Intent(this@MainActivity, QrActivity::class.java))
            // mari pakai inten untuk menuju ke halaman statistik!!!!!!!!
        } else if (v.getId() == R.id.nav_statistik) {
            startActivity(Intent(this@MainActivity, StatistikActivity::class.java))

            // inten ke halaman profil
        } else if (v.getId() == R.id.nav_profil) {
            startActivity(Intent(this@MainActivity, ProfilActivity::class.java))
        } else if (v.getId() == R.id.btn_tukar_poin) {
            startActivity(Intent(this@MainActivity, RewardActivity::class.java))
        }
    }

    //ini utk set data untuk 3 aktivitas yang baru
    // karena memakai <include> seperti sebelumnya, maka perlu
    // override datanya di sini:
    private fun setupAktivitasItems() {
        val itemBotol = findViewById<View>(R.id.item_botol) // (di Statistik sesuaikan ID nya)
        val itemKertas = findViewById<View>(R.id.item_kertas) // (di Statistik sesuaikan ID nya)

        service.riwayat!!.enqueue(object : Callback<MutableList<RiwayatResponse?>?> {
            override fun onResponse(
                call: Call<MutableList<RiwayatResponse?>?>,
                response: Response<MutableList<RiwayatResponse?>?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    val list: List<RiwayatResponse> = response.body()!!.filterNotNull()

                    if (list.size > 0) {
                        val r1 = list.get(0)
                        (itemBotol.findViewById<View?>(R.id.tv_nama_sampah) as TextView).setText(r1.namaSampah)
                        (itemBotol.findViewById<View?>(R.id.tv_berat) as TextView).setText(
                            r1.berat.toString() + " kg"
                        )
                        (itemBotol.findViewById<View?>(R.id.tv_poin) as TextView).setText("+" + r1.poinDidapat + " poin")
                        (itemBotol.findViewById<View?>(R.id.tv_tanggal_lokasi) as TextView).setText(
                            r1.tanggalLokasi
                        )
                    }

                    if (list.size > 1) {
                        val r2 = list.get(1)
                        (itemKertas.findViewById<View?>(R.id.tv_nama_sampah) as TextView).setText(r2.namaSampah)
                        (itemKertas.findViewById<View?>(R.id.tv_berat) as TextView).setText(
                            r2.berat.toString() + " kg"
                        )
                        (itemKertas.findViewById<View?>(R.id.tv_poin) as TextView).setText("+" + r2.poinDidapat + " poin")
                        (itemKertas.findViewById<View?>(R.id.tv_tanggal_lokasi) as TextView).setText(
                            r2.tanggalLokasi
                        )
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<RiwayatResponse?>?>, t: Throwable) {}
        })
    }

    //set data untuk 3 item edukasinya
    private fun setupEdukasiItems() {
        val item1 = findViewById<View>(R.id.item_edukasi_1)
        val item2 = findViewById<View>(R.id.item_edukasi_2)

        // Teks sementara selagi menunggu balasan dari Django
        (item1.findViewById<View?>(R.id.tv_judul_edukasi) as TextView).setText("Memuat video...")
        (item2.findViewById<View?>(R.id.tv_judul_edukasi) as TextView).setText("Memuat video...")

        // Tembak API Edukasi
        service.edukasi!!.enqueue(object : Callback<MutableList<EdukasiResponse?>?> {
            override fun onResponse(
                call: Call<MutableList<EdukasiResponse?>?>,
                response: Response<MutableList<EdukasiResponse?>?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    val list: List<EdukasiResponse> = response.body()!!.filterNotNull()

                    // Mengisi Item Edukasi Pertama (jika data di database tersedia)
                    if (list.size > 0) {
                        val edukasi1 = list.get(0)
                        (item1.findViewById<View?>(R.id.tv_judul_edukasi) as TextView).setText(
                            edukasi1.judul
                        )
                        (item1.findViewById<View?>(R.id.tv_desc_edukasi) as TextView).setText(
                            edukasi1.deskripsi
                        )

                        // Menempelkan ID YouTube ke tombol agar bisa ditonton
                        item1.findViewById<View?>(R.id.btn_tonton_video)
                            .setOnClickListener(View.OnClickListener { v: View? ->
                                bukaYoutube(edukasi1.videoIdYoutube)
                            })
                    }

                    // Mengisi Item Edukasi Kedua (jika data di database tersedia)
                    if (list.size > 1) {
                        val edukasi2 = list.get(1)
                        (item2.findViewById<View?>(R.id.tv_judul_edukasi) as TextView).setText(
                            edukasi2.judul
                        )
                        (item2.findViewById<View?>(R.id.tv_desc_edukasi) as TextView).setText(
                            edukasi2.deskripsi
                        )

                        // Menempelkan ID YouTube ke tombol agar bisa ditonton
                        item2.findViewById<View?>(R.id.btn_tonton_video)
                            .setOnClickListener(View.OnClickListener { v: View? ->
                                bukaYoutube(edukasi2.videoIdYoutube)
                            })
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<EdukasiResponse?>?>, t: Throwable) {
                // Bisa menampilkan Toast atau log jika gagal memuat data
            }
        })
    }

    // semoga bisa mengatasi masalah navbar punya warna berbeda setiap halaman
    private fun setActiveNav() {
        setNavColor(R.id.nav_home, R.color.green_primary, Typeface.BOLD)
        setNavColor(R.id.nav_reward, R.color.gray_text, Typeface.NORMAL)
        setNavColor(R.id.nav_statistik, R.color.gray_text, Typeface.NORMAL)
        setNavColor(R.id.nav_profil, R.color.gray_text, Typeface.NORMAL)
    }

    private fun setNavColor(navId: Int, colorRes: Int, typefaceStyle: Int) {
        val tab = findViewById<View?>(navId)
        if (tab == null) return
        if (tab is LinearLayout) {
            for (i in 0..<tab.getChildCount()) {
                val child = tab.getChildAt(i)
                if (child is ImageView) {
                    child.setColorFilter(
                        getResources().getColor(colorRes, getTheme())
                    )
                } else if (child is TextView) {
                    child.setTextColor(
                        getResources().getColor(colorRes, getTheme())
                    )
                    child.setTypeface(null, typefaceStyle)
                }
            }
        }
    }

    //INI UNTUK FITUR NONTON VIDEO YOUTUBE TUTORIAL
    //WEEE PAKAI IMPLICIT INTENT LAGIII
    // seperti kaya yang gojek itu, kalau blm ada appnya maka akan buka browser
    private fun bukaYoutube(videoId: String?) {
        // memcoba buka di app YouTube
        val appIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("vnd.youtube:" + videoId)
        )

        if (appIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(appIntent)
        } else {
            // fallback ke browser kalau tidak ada appnya
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://youtube.com/watch?v=" + videoId)
            )
            startActivity(webIntent)
        }
    }
}