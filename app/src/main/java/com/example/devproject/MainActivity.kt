package com.example.devproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import com.example.devproject.AddConferences.AddConferencesActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "TAG"
    }

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

        val transformedList = someList.map {

        }
        Log.d(TAG, "onCreate: $transformedList")

        addConferences()
    }

    private fun addConferences() {
        val addCon = findViewById<Button>(R.id.conferAddButton)
        addCon.setOnClickListener {
            startActivity(Intent(this, AddConferencesActivity::class.java))
        }
    }
}
