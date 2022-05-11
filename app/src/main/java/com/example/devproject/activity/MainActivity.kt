package com.example.devproject.activity

/**
 * Developers
 * LeeJaeHyeon05
 * jundonghyun
 * volta2030
 */

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.devproject.R
import com.example.devproject.activity.account.LoginActivity
import com.example.devproject.activity.account.ProfileActivity
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private var backPressedTime : Long = 0
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
                finish()
            }
            R.id.profileButton ->{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        UIHandler.allocateUI(window.decorView.rootView, this)
        UIHandler.activateUI(R.id.conferRecyclerView)

        val swipeRefreshLayout : SwipeRefreshLayout = findViewById(R.id.swiperRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            DataHandler.reload()
            swipeRefreshLayout.isRefreshing = false
        }

        addConferences()
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

    private fun addConferences() {
        val addCon = findViewById<Button>(R.id.conferAddButton)
        addCon.setOnClickListener {
            if(FirebaseIO.isValidAccount()){
                startActivity(Intent(this, AddConferencesActivity::class.java))
            }else {
                Toast.makeText(this, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }
    }
}