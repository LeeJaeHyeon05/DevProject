package com.example.devproject.util

import android.util.Log
import com.example.devproject.format.ConferenceInfo
import java.io.File

class DataHandler {
    companion object {

        var imageDataSet : MutableList<Array<File>> = emptyList<Array<File>>().toMutableList()
        var conferDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()

        //firebase load
        fun load() {
            FirebaseIO.read("conferenceDocument").addOnSuccessListener { result ->
                run {
                    for (document in result) {
                        conferDataSet.add(arrayOf(                   //index
                            document.data["uploader"] as String,     //0
                            document.data["title"] as String,        //1
                            document.data["date"] as String,         //2
                            document.data["price"] as Long,           //3
                            document.data["offline"] as Boolean,     //4
                            document.data["conferenceURL"] as String,//5
                            document.data["content"] as String       //6
                        )
                        )
                    }
                }
            }
        }

        fun reload(){
            delete()
            FirebaseIO.read("conferenceDocument").addOnSuccessListener { result ->
                run {
                    for (document in result) {
                        conferDataSet.add(arrayOf(                   //index
                            document.data["uploader"] as String,     //0
                            document.data["title"] as String,        //1
                            document.data["date"] as String,         //2
                            document.data["price"] as Long,           //3
                            document.data["offline"] as Boolean,     //4
                            document.data["conferenceURL"] as String,//5
                            document.data["content"] as String       //6
                        )
                        )
                    }
                }.run {
                    UIHandler.adapter!!.notifyDataSetChanged()
                }
            }
        }

        fun delete(){
            imageDataSet = emptyList<Array<File>>().toMutableList()
            conferDataSet = emptyList<Array<Any>>().toMutableList()
        }

        fun updateConferDataSet(conferenceInfo: ConferenceInfo){
            conferDataSet.add(conferenceInfo.toArray())
        }
    }

}