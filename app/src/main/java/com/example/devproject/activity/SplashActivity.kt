package com.example.devproject.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.devproject.R
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataHandler.load()

        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //나이트모드 적용 해제

        autoLoginValidate()

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }

    private fun autoLoginValidate(){

        val sharedPref = this.getSharedPreferences("saveAutoLoginChecked",
            MODE_PRIVATE
        ).getBoolean("CheckBox", false)
        val sharedEmail = this.getSharedPreferences("saveAutoLoginChecked",
            MODE_PRIVATE
        ).getString("Email", null)
        val sharedPassword = this.getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE).getString("Password", null)

        if (sharedEmail != null && sharedPassword != null &&sharedPref) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(sharedEmail, sharedPassword).addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "자동 로그인 되었습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}