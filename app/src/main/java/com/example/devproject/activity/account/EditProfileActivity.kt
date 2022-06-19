package com.example.devproject.activity.account

import android.R
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.adapter.LanguageListAdapter2
import com.example.devproject.adapter.PositionListAdapter
import com.example.devproject.databinding.ActivityEditProfileBinding
import com.example.devproject.dialog.BasicDialog
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var typedArray : TypedArray
    private lateinit var languageTypedArray: TypedArray
    private lateinit var positionListAdapter : PositionListAdapter
    private lateinit var languageListAdapter : LanguageListAdapter

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.example.devproject.R.menu.actionbar_edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home -> {
                finish()
                return true
            }

            com.example.devproject.R.id.editButton -> {
            UIHandler.profileImageView?.setImageDrawable(typedArray.getDrawable(positionListAdapter.convertKeyToIndex()))
            UIHandler.languageSelectRecyclerView?.adapter = LanguageListAdapter2(languageListAdapter.getLanguageList())

            FirebaseIO.db.collection("UserInfo").document(DataHandler.userInfo.id.toString()).update("language", languageListAdapter.getLanguageList())
            FirebaseIO.db.collection("UserInfo").document(DataHandler.userInfo.id.toString()).update("position", positionListAdapter.convertKeyToIndex().toLong())
            finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "프로필 편집"

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.positionTextView.text = convertIndexToString(DataHandler.userInfo.position!!.toInt())
        UIHandler.positionTextView = binding.positionTextView


        typedArray = resources.obtainTypedArray(com.example.devproject.R.array.position_array)
        var positionSelectRecyclerView = binding.positionSelectRecyclerView
        positionSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        positionListAdapter = PositionListAdapter(typedArray, DataHandler.userInfo.position!!.toInt())
        positionSelectRecyclerView?.adapter = positionListAdapter

        languageTypedArray = resources.obtainTypedArray(com.example.devproject.R.array.language_array)
        var languageRecyclerView = binding.languageRecyclerView
        languageRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        languageListAdapter = LanguageListAdapter(languageTypedArray, DataHandler.userInfo.language!!)
        languageRecyclerView?.adapter = languageListAdapter
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