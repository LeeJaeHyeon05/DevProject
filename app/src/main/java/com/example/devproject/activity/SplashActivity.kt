package com.example.devproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.example.devproject.R
import com.example.devproject.util.DataHandler
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //나이트모드 적용 해제
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            if(autoLoginValidate()){
                DataHandler.load()
                auth.currentUser?.reload()?.addOnCompleteListener { task -> //자동로그인시 계정이 정지되었는지 삭제되었는지 확인
                    if(task.isSuccessful){
                        Toast.makeText(this, "자동로그인 되었습니다", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "사용자 계정이 정지되었거나 삭제되었습니다. 관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }
            else{
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }

    private fun autoLoginValidate(): Boolean{
        val sharedPref = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE).getBoolean("CheckBox", false)
        val sharedId = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE).getString("Email", null)

        if (sharedId != null && sharedPref) {
            return true
        }
        return false
    }
}