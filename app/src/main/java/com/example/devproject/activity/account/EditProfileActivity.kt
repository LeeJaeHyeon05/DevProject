package com.example.devproject.activity.account

import android.R
import android.content.res.TypedArray
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.adapter.PositionListAdapter
import com.example.devproject.databinding.ActivityEditProfileBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.positionTextView.text = convertIndexToString(DataHandler.userInfo.position!!.toInt())
        UIHandler.positionTextView = binding.positionTextView


        var typedArray : TypedArray = resources.obtainTypedArray(com.example.devproject.R.array.position_array)
        var positionSelectRecyclerView = binding.positionSelectRecyclerView
        positionSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = PositionListAdapter(typedArray, DataHandler.userInfo.position!!.toInt())
        positionSelectRecyclerView?.adapter = adapter

        binding.profileEditDoneButton.setOnClickListener {
            UIHandler.profileImageView?.setImageDrawable(typedArray.getDrawable(adapter.convertKeyToIndex()))
            FirebaseIO.db.collection("UserInfo").document(DataHandler.userInfo.id.toString()).update("position", adapter.convertKeyToIndex().toLong())
            finish()
        }
    }

    private fun convertIndexToString(index : Int) : String{
        return when (index) {
            0 -> "프론트엔드"
            1 -> "백엔드"
            2 -> "풀스택"
            3 -> "임베디드"
            4 -> "퍼블리셔"
            else -> "프론트엔드"
        }
    }
}