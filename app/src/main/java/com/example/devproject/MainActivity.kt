package com.example.devproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataHandler.load()
        UIHandler.allocateUI(window.decorView.rootView)
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
    private fun addConferences() {
        val addCon = findViewById<Button>(R.id.conferAddButton)
        addCon.setOnClickListener {
            startActivity(Intent(this, addConferencesActivity::class.java))
        }
    }
}