package com.example.devproject.activity

/**
 * Developers
 * LeeJaeHyeon05
 * jundonghyun
 * volta2030
 */

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ListAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.R
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private var backPressedTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        UIHandler.allocateUI(window.decorView.rootView, this)
        UIHandler.activateUI(R.id.conferRecyclerView)

        var someList = mutableListOf<Int>()
        someList.add(0)
        someList.add(1)
        someList.add(2)
        someList.add(3)
        someList.add(4)
        someList.add(5)

        val transformedList =  someList.map {

        }
        Log.d(TAG, "onCreate: $transformedList")

        addConferences()
    }

//    override fun onResume() {
//        super.onResume()
//
//        UIHandler.activateUI(R.id.conferRecyclerView)
//    }
//
    override fun onRestart() {
        super.onRestart()
        getAllData()
        Thread.sleep(500)
    }

    private fun getAllData(){
        Thread{
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView(){
        runOnUiThread{
            UIHandler.activateUI(R.id.conferRecyclerView)
        }
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis() - backPressedTime >= 1500){
            backPressedTime = System.currentTimeMillis()
            Snackbar.make(window.decorView.rootView, "한번 더 눌러 종료합니다." , Snackbar.LENGTH_LONG).show()
        }else{
            DataHandler.delete()
            finish()
        }
    }


    private fun addConferences() {
        val addCon = findViewById<Button>(R.id.conferAddButton)
        addCon.setOnClickListener {
//            var startAddConferenceActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result?.resultCode ?: 0 == Activity.RESULT_OK) {
//                    DataHandler.load()
//                }
//            }
            startActivity(Intent(this, AddConferencesActivity()::class.java))
        }
    }
}