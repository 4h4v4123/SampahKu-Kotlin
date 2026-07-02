package com.example.sampahku

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * jadiiiiiiiiii, file ini utk apa? Ini file utk broadcast receiver yg dipakai
 * untuk menangani berbagai event sistem dari OS Android.
 * [INDIKATOR UAS, BONUS SCORE] Menunjukkan kemampuan aplikasi merespon sinyal sistem secara otomatis (Notifikasi).
 */
class SystemReceiver : BroadcastReceiver() {

    /**
     * onReceive, ini akan kepanggil otomatis saat Android mengirimkan broadcast yang sesuai dengan filter.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // pastikan channel notifikasi siap digunakan
        NotificationHelper.createNotificationChannel(context)

        // BTW semua teks-teksnya ada di file string
        when (intent.action) {
            // [IMPLEMENTASI IDE 2] akan deteksi kalau baterai HP berada di level rendah (biasanya < 15-20%)
            Intent.ACTION_BATTERY_LOW -> {
                NotificationHelper.sendNotification(
                    context,
                    context.getString(R.string.notif_battery_title),
                    context.getString(R.string.notif_battery_msg),
                    101
                )
            }

            // [IMPLEMENTASI IDE 4] deteksi kalau HP mulai dihubungkan ke charger
            // ALSO BAHASA HELP, tulisnya cas atau charge
            // cas aja lah >.<
            Intent.ACTION_POWER_CONNECTED -> {
                NotificationHelper.sendNotification(
                    context,
                    context.getString(R.string.notif_power_title),
                    context.getString(R.string.notif_power_msg),
                    102
                )
            }

            // [IMPLEMENTASI IDE 6] deteksi di saat mode pesawat (airplane mode) dinyalakan atau dimatikan oleh user
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                NotificationHelper.sendNotification(
                    context,
                    context.getString(R.string.notif_airplane_title),
                    context.getString(R.string.notif_airplane_msg),
                    103
                )
            }

            // [IMPLEMENTASI IDE 3] deteksi ada perubahan status internet (Online/Offline) secara real-time
            // (couldve been the airplane mode too but whatever...
            // extra work but homestly I couldnt care less
            ConnectivityManager.CONNECTIVITY_ACTION -> {
                if (isOnline(context)) {
                    NotificationHelper.sendNotification(
                        context,
                        context.getString(R.string.notif_network_online_title),
                        context.getString(R.string.notif_network_online_msg),
                        104
                    )
                } else {
                    NotificationHelper.sendNotification(
                        context,
                        context.getString(R.string.notif_network_offline_title),
                        context.getString(R.string.notif_network_offline_msg),
                        104
                    )
                }
            }
        }
    }

    /**
     * isOnline, sebuah helper utk mengecek status koneksi perangkat ke internet.
     */
    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo != null && networkInfo.isConnected
        }
    }
}
