package com.example.devproject.activity.account

import android.R
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.adapter.PositionListAdapter
import com.example.devproject.databinding.ActivityEditProfileBinding
import com.example.devproject.util.UIHandler
import kotlinx.android.synthetic.main.activity_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UIHandler.positionTextView = binding.positionTextView
        var typedArray : TypedArray = resources.obtainTypedArray(com.example.devproject.R.array.position_array)
        var positionSelectRecyclerView = binding.positionSelectRecyclerView
        positionSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = PositionListAdapter(typedArray, null)
        positionSelectRecyclerView?.adapter = adapter

        binding.profileEditDoneButton.setOnClickListener {
            UIHandler.profileImageView?.setImageDrawable(typedArray.getDrawable(adapter.convertKeyToIndex()))
            finish()
        }

    }
}