package com.example.devproject.activity.account

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        var logoutButton : Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onPause() {
        super.onPause()
        val savePref = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE)
        savePref.edit().putBoolean("CheckBox", false).apply()
    }
}