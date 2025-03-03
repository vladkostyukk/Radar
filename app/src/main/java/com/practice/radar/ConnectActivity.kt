package com.practice.radar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.radar.adapters.AvailableWifiAdapter

class ConnectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_connect)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val availableWifiRecyclerView =
            findViewById<RecyclerView>(R.id.available_wifi_recycler_view)
        availableWifiRecyclerView.layoutManager = LinearLayoutManager(this)
        availableWifiRecyclerView.setHasFixedSize(true)
        availableWifiRecyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                LinearLayoutManager.VERTICAL
            )
        )

        availableWifiRecyclerView.adapter = AvailableWifiAdapter(
            listOf("Сеть 1", "Сеть 2", "Сеть 3", "Сеть 4", "Сеть 5")
        )

        val connectButton = findViewById<Button>(R.id.connect_button)
        connectButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}