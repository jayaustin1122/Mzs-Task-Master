package com.example.mzstaskmasterfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mzstaskmasterfinal.databinding.ActivityMainBinding
import com.example.mzstaskmasterfinal.ui.AboutFragment
import com.example.mzstaskmasterfinal.ui.HistoryFragment
import com.example.mzstaskmasterfinal.ui.HomeFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    //declare
    private lateinit var drawerLayout : DrawerLayout

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //init
        drawerLayout = binding.drawerlayout

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav,R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null){
            replaceFragment(HomeFragment(),getString(R.string.title_home))
            navigationView.setCheckedItem(R.id.nav_home)
        }
    }
    private fun replaceFragment(fragment: Fragment,title: String){
        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container,fragment)
        transaction.commit()
        supportActionBar?.title = title
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment(), getString(R.string.title_home))
            R.id.nav_history -> replaceFragment(HistoryFragment(), getString(R.string.title_history))
            R.id.nav_about -> replaceFragment(AboutFragment(), getString(R.string.title_about))
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}


