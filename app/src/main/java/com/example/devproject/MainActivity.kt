package com.example.devproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import com.example.devproject.AddConferences.AddConferencesActivity

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG = "TAG"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DataHandler.load()
        UIHandler.allocateUI(window.decorView.rootView)
        UIHandler.activateUI(R.id.conferRecyclerView)

        someFunction()
        someLamdaFunction.invoke()

        someFunctionWithParam("매개변수")
        someLamdaFunctionWithParam("람다 매개변수")

        someLamdaFunctionWithMultiParams("여러매개변수", 1)

        Log.d(TAG, "${someLamdaFuncationWithReturn(3, 5)}")

//        this.someFunctionWithLamda{
//            Log.d(TAG, "onCreate: 3초가 지났습니다")
//        }
//
//        this.someFunctionWithParamAndLamda(3, completion = {
//            Log.d(TAG, "onCreate: 받은값이 $it 입니다")
//        })

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
            startActivity(Intent(this, AddConferencesActivity::class.java))
        }
    }

    fun someFunction(){
        Log.d("TAG", "someFunction: () called")
    }

    val someLamdaFunction: () -> Unit = {
        Log.d("TAG", "someLamdaFunction: () called")
    }
