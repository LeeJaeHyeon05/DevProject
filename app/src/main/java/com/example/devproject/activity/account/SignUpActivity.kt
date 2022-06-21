package com.example.devproject.activity.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.devproject.R
import com.example.devproject.util.KeyboardVisibilityUtils
import com.example.devproject.databinding.ActivitySignUpBinding
import com.example.devproject.fragment.SignUpNecessaryFragment
import com.example.devproject.fragment.SignUpOptionalFragment
import com.example.devproject.util.UIHandler.Companion.signUpNecessaryFragment
import com.example.devproject.util.UIHandler.Companion.signUpOptionalFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var menu : Menu
    lateinit var binding : ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.title = "회원가입"

        signUpNecessaryFragment = SignUpNecessaryFragment()
        signUpOptionalFragment = SignUpOptionalFragment()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

