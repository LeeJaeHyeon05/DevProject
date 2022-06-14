package com.example.devproject.activity.conference

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.devproject.R
import com.example.devproject.databinding.ActivityShowDetailImageBinding
import kotlin.math.max
import kotlin.math.min

class ShowDetailImage : AppCompatActivity() {

    lateinit var binding: ActivityShowDetailImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var image = intent.getStringExtra("image")

        Glide.with(this)
            .load(Uri.parse(image))
            .into(binding.detailImage)
    }
}