package com.practice.radar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.practice.radar.fragments.AdminFragment
import com.practice.radar.fragments.ConditionFragment
import com.practice.radar.fragments.MaintenanceFragment
import com.practice.radar.fragments.ReportFragment
import com.practice.radar.receiver.PowerConnectionReceiver

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private val powerReceiver = PowerConnectionReceiver()
    private val adminReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "SHOW_ADMIN_TAB" -> showAdminDialog()
                "HIDE_ADMIN_TAB" -> hideAdminTab()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navigationView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setCheckedItem(R.id.condition_item)

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.condition_item -> {
                    replaceFragment(ConditionFragment())
                }
                R.id.maintenance_service_item -> {
                    replaceFragment(MaintenanceFragment())
                }
                R.id.report_item -> {
                    replaceFragment(ReportFragment())
                }
                R.id.admin_item ->{
                    selectAdminTab()
                }
            }

            drawerLayout.closeDrawers()
            true
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    finish()
                }
            }
        })

        if (savedInstanceState == null) {
            replaceFragment(ConditionFragment())
        }

        registerReceiver(powerReceiver, IntentFilter(Intent.ACTION_POWER_CONNECTED))
        registerReceiver(powerReceiver, IntentFilter(Intent.ACTION_POWER_DISCONNECTED))

        val adminFilter = IntentFilter().apply {
            addAction("SHOW_ADMIN_TAB")
            addAction("HIDE_ADMIN_TAB")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) registerReceiver(adminReceiver, adminFilter, Context.RECEIVER_NOT_EXPORTED)
    }
    private fun showAdminDialog() {
        AlertDialog.Builder(this)
            .setTitle("Панель администратора")
            .setMessage("Перейти в панель администратора?")
            .setPositiveButton("Да") { _, _ -> selectAdminTab() }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun selectAdminTab() {
        val menu = navigationView.menu
        val adminItem = menu.findItem(R.id.admin_item)

        adminItem.isVisible = true
        navigationView.setCheckedItem(R.id.admin_item)

        updateToolbarTitle(R.string.admin)
        replaceFragment(AdminFragment())
    }

    private fun hideAdminTab() {
        val menu = navigationView.menu
        val adminItem = menu.findItem(R.id.admin_item)

        adminItem.isVisible = false
        navigationView.setCheckedItem(R.id.condition_item)

        updateToolbarTitle(R.string.app_name)
        replaceFragment(ConditionFragment())
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(powerReceiver)
        unregisterReceiver(adminReceiver)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun updateToolbarTitle(titleId: Int) {
        toolbar.setTitle(titleId)
    }
}