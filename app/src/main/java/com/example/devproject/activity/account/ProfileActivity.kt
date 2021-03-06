package com.example.devproject.activity.account

import android.content.Intent
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.example.devproject.format.UserInfo
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = DataHandler.userInfo.id + "의 프로필"

        var logoutButton : Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        val profileImageView : ImageView = findViewById(R.id.profileImageView)
        profileImageView.setImageResource(R.drawable.logo512)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        DataHandler.userInfo = UserInfo()
        Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}