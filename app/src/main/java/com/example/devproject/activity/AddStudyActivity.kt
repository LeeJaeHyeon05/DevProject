package com.example.devproject.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.databinding.ActivityAddStudyBinding

class AddStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}