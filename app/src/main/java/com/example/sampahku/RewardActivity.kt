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
import kotlin.math.min

// apa perlu ya?
//agar bisa intent implicit
class RewardActivity : AppCompatActivity(), View.OnClickListener {
    private var ivBack: ImageView? = null

    // list navbar isinya
    private var navHome: LinearLayout? = null
    private var navReward: LinearLayout? = null
    private var navQr: LinearLayout? = null
    private var navStatistik: LinearLayout? = null
    private var navProfil: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        if (getSupportActionBar() != null) {
            getSupportActionBar()!!.hide()
        }

        // inisialisasi tombol back
        // masih bermasalah sih...
        // NVM.. FIXED!
        ivBack = findViewById<ImageView>(R.id.iv_back)
        ivBack!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish()
            }
        })

        // inisialisasi bottom navigation bar
        navHome = findViewById<LinearLayout>(R.id.nav_home)
        navReward = findViewById<LinearLayout>(R.id.nav_reward)
        navQr = findViewById<LinearLayout>(R.id.nav_qr)
        navStatistik = findViewById<LinearLayout>(R.id.nav_statistik)
        navProfil = findViewById<LinearLayout>(R.id.nav_profil)

        navHome!!.setOnClickListener(this)
        navReward!!.setOnClickListener(this)
        navQr!!.setOnClickListener(this)
        navStatistik!!.setOnClickListener(this)
        navProfil!!.setOnClickListener(this)

        // set data untuk item-item rewardnya
        setupRewardItems()
        setActiveNav() // untuk navbar ganti warna ketika di halaman reward
    }

    override fun onClick(v: View) {
        if (v.getId() == R.id.nav_home) {
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        } else if (v.getId() == R.id.nav_reward) {
            // sudah di Reward, jadi ya...
            Toast.makeText(this, "Reward", Toast.LENGTH_SHORT).show()
        } else if (v.getId() == R.id.nav_qr) {
            startActivity(Intent(this@RewardActivity, QrActivity::class.java))
        } else if (v.getId() == R.id.nav_statistik) {
            startActivity(Intent(this@RewardActivity, StatistikActivity::class.java))
        } else if (v.getId() == R.id.nav_profil) {
            startActivity(Intent(this@RewardActivity, ProfilActivity::class.java))
        }
    }

    // ini utk melakukan set data untuk semua item reward
    // (tapi karena udh pakai <include>, kita override datanya di sini)
    private fun setupRewardItems() {
        val itemLast = findViewById<View?>(R.id.item_last_reward)
        val item1 = findViewById<View?>(R.id.item_reward_1)
        val item2 = findViewById<View?>(R.id.item_reward_2)
        val item3 = findViewById<View?>(R.id.item_reward_3)

        val itemViews = arrayOf<View>(itemLast!!, item1!!, item2!!, item3!!)

        service.reward!!.enqueue(object : Callback<MutableList<RewardResponse?>?> {
            override fun onResponse(
                call: Call<MutableList<RewardResponse?>?>,
                response: Response<MutableList<RewardResponse?>?>
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    val list: List<RewardResponse> = response.body()!!.filterNotNull()

                    for (i in 0..<min(list.size, itemViews.size)) {
                        val reward = list.get(i)
                        val itemView = itemViews[i]

                        (itemView.findViewById<View?>(R.id.tv_reward_name) as TextView).setText(
                            reward.namaReward
                        )
                        (itemView.findViewById<View?>(R.id.tv_reward_desc) as TextView).setText(
                            reward.deskripsi
                        )
                        (itemView.findViewById<View?>(R.id.tv_reward_points) as TextView).setText(
                            reward.poinDibutuhkan.toString() + " Poin"
                        )

                        val resId = getResources().getIdentifier(
                            reward.logoResourceName,
                            "drawable",
                            getPackageName()
                        )
                        if (resId != 0) {
                            (itemView.findViewById<View?>(R.id.iv_reward_logo) as ImageView).setImageResource(
                                resId
                            )
                        }

                        if (reward.logoResourceName?.contains("gopay") == true) {
                            itemView.findViewById<View?>(R.id.btn_tukar)
                                .setOnClickListener(View.OnClickListener { v: View? -> bukaGopay() })
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MutableList<RewardResponse?>?>, t: Throwable) {}
        })
    }

    /*
     * jadi ini bentuk implicit intent untuk membuka aplikasi GoPay.
     * "aplikasi" padahal cuma websitenya saja
     * kalau terinstall ya, akan langsung kebuka app-nya.
     */
    private fun bukaGopay() {
        // coba buka app GoPay langsung via deep link (kalau ada)
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("gojek://gopay")
        )

        // cek apa ada app yang bisa handle intent ini
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent) // maka buka app GoPay
        } else {
            // fallback: buka browser ke website GoPay
            val browser = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://gopay.co.id")
            )
            startActivity(browser)
        }
    }

    private fun setActiveNav() {
        setNavColor(R.id.nav_home, R.color.gray_text, Typeface.NORMAL)
        setNavColor(R.id.nav_reward, R.color.green_primary, Typeface.BOLD)
        setNavColor(R.id.nav_statistik, R.color.gray_text, Typeface.NORMAL)
        setNavColor(R.id.nav_profil, R.color.gray_text, Typeface.NORMAL)
    }

    private fun setNavColor(navId: Int, colorRes: Int, typefaceStyle: Int) {
        val tab = findViewById<LinearLayout?>(navId)
        if (tab == null) return
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