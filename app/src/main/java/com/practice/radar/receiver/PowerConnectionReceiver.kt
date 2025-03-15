package com.practice.radar.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PowerConnectionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d("PowerReceiver", "Питание подключено, предлагаем перейти в админ панель")

                val mainIntent = Intent("SHOW_ADMIN_TAB")
                context?.sendBroadcast(mainIntent)
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d("PowerReceiver", "Питание отключено, закрываем админ панель")

                val mainIntent = Intent("HIDE_ADMIN_TAB")
                context?.sendBroadcast(mainIntent)
            }
        }
    }
}
