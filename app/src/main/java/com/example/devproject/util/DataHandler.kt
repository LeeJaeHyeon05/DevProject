package com.example.devproject.util

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.devproject.R
import com.example.devproject.format.UserInfo
import com.example.devproject.fragment.HomeFragment
import com.example.devproject.fragment.StudyFragment
import com.example.devproject.others.DBType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class DataHandler {
    companion object {

        var imageDataSet : MutableList<Array<File>> = emptyList<Array<File>>().toMutableList()
        var conferDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()
        var studyDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()
        var userInfo = UserInfo()

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
                                    document.data["finishDate"] as String,   //11
                                    document.data["place"] as String //12
                                )
                                )
                            }
                        }
                    }
                }
                DBType.STUDY -> {
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        studyDataSet.clear()
                        for (document in result) {
                            studyDataSet.add(arrayOf(
                                document.data["ongoing"] as Boolean,
                                document.data["uploader"] as String,
                                document.data["title"] as String,
                                document.data["content"] as String,
                                document.data["offline"] as Boolean,
                                document.data["studyURL"] as String,
                                document.data["totalMember"] as Long,
                                document.data["remainingMemeber"] as Long,
                                document.data["language"] as MutableList<String>,
                                document.data["documentID"] as String,
                                document.data["uid"] as String,
                                document.data["endDate"] as String
                            )
                            )
                        }
                    }

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
                                    document.data["finishDate"] as String,   //11
                                    document.data["place"] as String //12
                                )
                                )
                            }
                        }.run {
                              HomeFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
                DBType.STUDY->{
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        run {
                            studyDataSet.clear()
                            for (document in result) {
                                studyDataSet.add(
                                    arrayOf(
                                        document.data["ongoing"] as Boolean,
                                        document.data["uploader"] as String,
                                        document.data["title"] as String,
                                        document.data["content"] as String,
                                        document.data["offline"] as Boolean,
                                        document.data["studyURL"] as String,
                                        document.data["totalMember"] as Long,
                                        document.data["remainingMemeber"] as Long,
                                        document.data["language"] as MutableList<String>,
                                        document.data["documentID"] as String,
                                        document.data["uid"] as String,
                                        document.data["endDate"] as String
                                    )
                                )
                            }
                        }.run {
                            StudyFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
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
                            HomeFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
                }
                DBType.STUDY->{
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        run {
                            studyDataSet.clear()
                            for (document in result) {
                                studyDataSet.add(
                                    arrayOf(
                                        document.data["ongoing"] as Boolean,
                                        document.data["uploader"] as String,
                                        document.data["title"] as String,
                                        document.data["offline"] as Boolean,
                                        document.data["studyURL"] as String,
                                        document.data["totalMember"] as Long,
                                        document.data["remainingMemeber"] as Long,
                                        document.data["documentID"] as String,
                                        document.data["uid"] as String
                                    )
                                )
                            }
                        }.run {
                            StudyFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
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
                    studyDataSet.clear()
                }
                else->{}
            }

        }

        fun updateUserInfo(){
            FirebaseFirestore.getInstance().collection("UserInfo")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser?.email)
                .get()
                .addOnSuccessListener {
                    for(document in it){
                        userInfo.id = document["id"] as String
                    }
                }
                .addOnFailureListener{
                    Log.d("TAG", "findUploader: ${it.stackTrace}")
                }
        }

    }

}