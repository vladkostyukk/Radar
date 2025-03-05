package com.practice.radar

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.practice.radar.fragments.ConditionFragment
import com.practice.radar.fragments.MaintenanceFragment
import com.practice.radar.fragments.ReportFragment

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar

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
                    replaceFragment(ConditionFragment(), R.string.condition)
                }
                R.id.maintenance_service_item -> {
                    replaceFragment(MaintenanceFragment(), R.string.maintenance_service)
                }
                R.id.report_item -> {
                    replaceFragment(ReportFragment(), R.string.report)
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
            replaceFragment(ConditionFragment(), R.string.condition)
        }
    }

    private fun replaceFragment(fragment: Fragment, titleId: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        toolbar.setTitle(titleId)
    }
}