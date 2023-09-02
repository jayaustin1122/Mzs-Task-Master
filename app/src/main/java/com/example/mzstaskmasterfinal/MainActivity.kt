package com.example.mzstaskmasterfinal

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mzstaskmasterfinal.databinding.ActivityMainBinding
import com.example.mzstaskmasterfinal.ui.AddTaskFragment
import com.example.mzstaskmasterfinal.ui.HistoryFragment
import com.example.mzstaskmasterfinal.ui.HomeFragment
import com.google.android.material.navigation.NavigationView
import kotlin.system.exitProcess

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
    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }
    // Override the onBackPressed method to handle back button presses

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> replaceFragment(HomeFragment(), getString(R.string.title_home))
            R.id.nav_history -> replaceFragment(HistoryFragment(), getString(R.string.title_about))
            R.id.nav_about ->showExitConfirmationDialog()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit Confirmation")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { _, _ ->
                // User confirmed to exit, so close the app
                finish() // If you're in an activity, finish the activity
            }
            .setNegativeButton("No") { dialog, _ ->
                // User canceled the exit, so dismiss the dialog
                dialog.dismiss()
            }
            .show()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_container)

            // Check if the current fragment is AddTaskFragment
            if (currentFragment is AddTaskFragment) {
                // Change the toolbar title to "Home" when going back to HomeFragment
                setToolbarTitle(getString(R.string.title_home))
                val homeFragment = HomeFragment()

                // Replace the current fragment with HomeFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .commit()
            } else {
                super.onBackPressed()
            }
        }
    }
}


