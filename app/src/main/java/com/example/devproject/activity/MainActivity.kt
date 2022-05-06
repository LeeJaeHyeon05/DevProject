package com.example.devproject.activity

/**
 * Developers
 * LeeJaeHyeon05
 * jundonghyun
 * volta2030
 */

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.devproject.R
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.others.ListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler
import com.google.android.material.snackbar.Snackbar
import java.lang.String
import kotlin.Int
import kotlin.Long

class MainActivity : AppCompatActivity() {
    private var backPressedTime : Long = 0
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item?.itemId){
            R.id.loginButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIHandler.allocateUI(window.decorView.rootView, this)
        UIHandler.activateUI(R.id.conferRecyclerView)

        val swipeRefreshLayout : SwipeRefreshLayout = findViewById(R.id.swiperRefreshLayout)

        swipeRefreshLayout.setOnRefreshListener {
            DataHandler.reload()
            swipeRefreshLayout.isRefreshing = false
        }

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
            startActivity(Intent(this, AddConferencesActivity::class.java))
        }
    }
}