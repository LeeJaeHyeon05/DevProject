package com.example.devproject.activity

/**
 * Developers
 * LeeJaeHyeon05
 * jundonghyun
 * volta2030
 */

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.devproject.R
import com.example.devproject.activity.account.LoginActivity
import com.example.devproject.activity.account.ProfileActivity
import com.example.devproject.databinding.ActivityMainBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var backPressedTime : Long = 0
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount()) {
            menuInflater.inflate(R.menu.actionbar_logined_menu, menu)
        }else{
            menuInflater.inflate(R.menu.actionbar_public_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.loginButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.profileButton ->{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        UIHandler.allocateUI(window.decorView.rootView, this)

        //navigation host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        //navigation controller
        val navController = navHostFragment.navController
        //binding bottom navigation view & navigation
        NavigationUI.setupWithNavController(mBinding.bottomNav, navController)
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 1500){
            backPressedTime = System.currentTimeMillis()
            Snackbar.make(window.decorView.rootView, "한번 더 눌러 종료합니다." , Snackbar.LENGTH_LONG).show()
        }else{
            DataHandler.delete()
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

}