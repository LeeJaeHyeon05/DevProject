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

        var memberNumberPicker = binding.memberNumberPicker
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }
        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data
        
        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->  }
        }

}
