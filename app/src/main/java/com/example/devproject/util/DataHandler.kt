package com.example.devproject.util

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.devproject.R
import com.example.devproject.fragment.HomeFragment
import com.example.devproject.others.DBType
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DataHandler {
    companion object {

        var imageDataSet : MutableList<Array<File>> = emptyList<Array<File>>().toMutableList()
        var conferDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()
        var studyDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()

        val filterList : MutableList<Any> = mutableListOf(0)

        @RequiresApi(Build.VERSION_CODES.O)
        var currentDate = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(
            DateTimeFormatter.ofPattern("yyyyMMdd")).toString()

        //firebase load
        fun load(type : DBType) {
            when(type){
                DBType.CONFERENCE -> {
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
                                    document.data["uid"] as String,
                                    document.data["documentID"] as String,
                                    document.data["image"] as MutableList<*>,
                                    document.data["startDate"] as String,   //10
                                    document.data["finishDate"] as String   //11
                                )
                                )
                            }
                        }
                    }
                }
                DBType.STUDY -> {}
                else -> {

                }
            }
        }

        fun reload(type : DBType){
            delete(type)
            when(type){
                DBType.CONFERENCE -> {
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
                                    document.data["uid"] as String,
                                    document.data["documentID"] as String,
                                    document.data["image"] as MutableList<*>,
                                    document.data["startDate"] as String,   //10
                                    document.data["finishDate"] as String   //11
                                )
                                )
                            }
                        }.run {
                              HomeFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
                DBType.STUDY->{
                }
            }
        }

        fun reload(type : DBType, filterList : MutableList<Any>){
            delete(type)
            when(type){
                DBType.CONFERENCE -> {
                    FirebaseIO.readPublic("conferenceDocument", filterList).addOnSuccessListener { result ->
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
                                    document.data["uid"] as String,
                                    document.data["documentID"] as String,
                                    document.data["image"] as MutableList<*>
                                )
                                )
                            }
                        }.run {
                            UIHandler.adapter!!.notifyDataSetChanged()
                            UIHandler.activateUI(R.id.conferRecyclerView)
                        }
                    }
                }
                DBType.STUDY->{
                }
            }
        }

        fun delete(type : DBType){
            when(type){
                DBType.CONFERENCE->{
                    imageDataSet.clear()
                    conferDataSet.clear()
                }
                DBType.STUDY->{

                }
                else->{}
            }

        }
    }

}