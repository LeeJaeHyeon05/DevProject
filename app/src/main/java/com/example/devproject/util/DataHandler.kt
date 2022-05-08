package com.example.devproject.util

import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.activity.LoginActivity
import com.example.devproject.activity.MainActivity
import com.example.devproject.format.ConferenceInfo
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DataHandler {
    companion object {

        var imageDataSet : MutableList<Array<File>> = emptyList<Array<File>>().toMutableList()
        var conferDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()
        @RequiresApi(Build.VERSION_CODES.O)
        var currentDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(
            DateTimeFormatter.ofPattern("yyyyMMdd")).toString()

        //firebase load
        fun load() {
            FirebaseIO.readPublic("conferenceDocument").addOnSuccessListener { result ->
                run {
                    conferDataSet.clear()
                    for (document in result) {
                        conferDataSet.add(arrayOf(                   //index
                            document.data["uploader"] as String,     //0
                            document.data["title"] as String,        //1
                            document.data["date"] as String,         //2
                            document.data["price"] as Long,           //3
                            document.data["offline"] as Boolean,     //4
                            document.data["conferenceURL"] as String,//5
                            document.data["content"] as String,       //6
                            document.data["uid"] as String
                        )
                        )
                    }
                }
            }
        }

        fun reload(){
            delete()
            FirebaseIO.readPublic("conferenceDocument").addOnSuccessListener { result ->
                run {
                    for (document in result) {
                        conferDataSet.add(arrayOf(                   //index
                            document.data["uploader"] as String,     //0
                            document.data["title"] as String,        //1
                            document.data["date"] as String,         //2
                            document.data["price"] as Long,           //3
                            document.data["offline"] as Boolean,     //4
                            document.data["conferenceURL"] as String,//5
                            document.data["content"] as String,       //6
                            document.data["uid"] as String
                            )
                        )
                    }
                }.run {
                    UIHandler.adapter!!.notifyDataSetChanged()
                }
            }
        }

        fun delete(){
            imageDataSet.clear()
            conferDataSet.clear()
        }
    }

}