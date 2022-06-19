package com.example.devproject.util

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.format.StudyInfo
import com.example.devproject.format.UserInfo
import com.example.devproject.fragment.HeadhuntingFragment
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
        var studyDataSet : MutableList<StudyInfo> = emptyList<StudyInfo>().toMutableList()
        var headhuntingDataSet : MutableList<Array<Any>> = emptyList<Array<Any>>().toMutableList()
        var conferDataSet : MutableList<ConferenceInfo> = emptyList<ConferenceInfo>().toMutableList()


        var conferenceNotiDeviceIDList : MutableList<String> = emptyList<String>().toMutableList()
        var studyNotiDeviceIDList : MutableList<String> = emptyList<String>().toMutableList()
        var headhuntingUserList : MutableList<String> = emptyList<String>().toMutableList()

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
                        conferDataSet.clear()
                        for (document in result) {
                            conferDataSet.add(
                                    ConferenceInfo(
                                        uploader =  document.data["uploader"] as String,     //0
                                        title =  document.data["title"] as String,        //1
                                        date =  document.data["date"] as String,         //2
                                        price =  document.data["price"] as Long,           //3
                                        offline = document.data["offline"] as Boolean,     //4
                                        conferenceURL = document.data["conferenceURL"] as String,//5
                                        content = document.data["content"] as String,       //6
                                        uid = document.data["uid"] as String,
                                        documentID =  document.data["documentID"] as String,
                                        image = document.data["image"] as MutableList<Uri>,
                                        startDate = document.data["startDate"] as String,   //10
                                        finishDate = document.data["finishDate"] as String,   //11
                                        place = document.data["place"] as String, //12
                                        manager = document.data["manager"] as Boolean
                                    )
                            )
                        }
                    }
                }

                DBType.STUDY -> {
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        studyDataSet.clear()
                        for (document in result) {
                            studyDataSet.add(
                                StudyInfo(
                                ongoing = document.data["ongoing"] as Boolean,
                                uploader = document.data["uploader"] as String,
                                title = document.data["title"] as String,
                                content = document.data["content"] as String,
                                offline = document.data["offline"] as Boolean,
                                studyURL = document.data["studyURL"] as String,
                                totalMember =  document.data["totalMember"] as Long,
                                remainingMemeber = document.data["remainingMemeber"] as Long,
                                language = document.data["language"] as MutableList<String>,
                                documentID = document.data["documentID"] as String,
                                uid = document.data["uid"] as String,
                                endDate = document.data["endDate"] as String
                            )
                            )
                        }
                    }

                }

                DBType.HEADHUNTING -> {
                        FirebaseIO.db.collection("UserInfo").get().addOnSuccessListener { result->
                            for(document in result){
                                if( headhuntingUserList.contains(document.id)){
                                    headhuntingDataSet.add(arrayOf(
                                        document.data["position"] as Long,
                                        document.data["email"] as String,
                                        document.data["language"] as MutableList<String>
                                        )
                                    )
                                }
                            }
                        }
                }
            }
        }

        fun loadNotiInformation(){
            FirebaseIO.readPublic("onesignalInfo").addOnSuccessListener {
                result->
                for (document in result){
                    when(document.id){
                        "conferenceNotification" -> {
                            conferenceNotiDeviceIDList = document.data["deviceID"] as MutableList<String>
                        }
                        "studyNotification" -> {
                            studyNotiDeviceIDList = document.data["deviceID"] as MutableList<String>
                        }
                    }
                }
            }
        }

        fun loadHeadhuntingInformation() {
            headhuntingUserList.clear()
            FirebaseIO.db.collection("etc").document("headhunting").get().addOnSuccessListener {
                headhuntingUserList = it["users"] as MutableList<String>
            }
        }

        fun reload(type : DBType){
            delete(type)
            when(type){
                DBType.CONFERENCE -> {
                    FirebaseIO.readPublic("conferenceDocument").addOnSuccessListener { result ->
                        run {
                            for (document in result) {
                                conferDataSet.add(
                                    ConferenceInfo(
                                        uploader =  document.data["uploader"] as String,     //0
                                        title =  document.data["title"] as String,        //1
                                        date =  document.data["date"] as String,         //2
                                        price =  document.data["price"] as Long,           //3
                                        offline = document.data["offline"] as Boolean,     //4
                                        conferenceURL = document.data["conferenceURL"] as String,//5
                                        content = document.data["content"] as String,       //6
                                        uid = document.data["uid"] as String,
                                        documentID =  document.data["documentID"] as String,
                                        image = document.data["image"] as MutableList<Uri>,
                                        startDate = document.data["startDate"] as String,   //10
                                        finishDate = document.data["finishDate"] as String,   //11
                                        place = document.data["place"] as String, //12
                                        manager = document.data["manager"] as Boolean
                                    )
                                )
                            }
                        }.run {
                              HomeFragment.adapterConference!!.notifyDataSetChanged()
                        }
                    }
                }
                DBType.STUDY->{
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        run {
                            studyDataSet.clear()
                            for (document in result) {
                                studyDataSet.add(
                                    StudyInfo(
                                        ongoing = document.data["ongoing"] as Boolean,
                                        uploader = document.data["uploader"] as String,
                                        title = document.data["title"] as String,
                                        content = document.data["content"] as String,
                                        offline = document.data["offline"] as Boolean,
                                        studyURL = document.data["studyURL"] as String,
                                        totalMember =  document.data["totalMember"] as Long,
                                        remainingMemeber = document.data["remainingMemeber"] as Long,
                                        language = document.data["language"] as MutableList<String>,
                                        documentID = document.data["documentID"] as String,
                                        uid = document.data["uid"] as String,
                                        endDate = document.data["endDate"] as String
                                    )
                                )
                            }
                        }.run {
                            StudyFragment.adapter!!.notifyDataSetChanged()
                        }
                    }
                }

                DBType.HEADHUNTING->{
                        FirebaseIO.db.collection("UserInfo").get().addOnSuccessListener { result ->
                            run {
                                for (document in result) {
                                    if (headhuntingUserList.contains(document.id)) {
                                        headhuntingDataSet.add(
                                            arrayOf(
                                                document.data["position"] as Long,
                                                document.data["email"] as String,
                                                document.data["language"] as MutableList<String>
                                            )
                                        )
                                    }
                                }
                            }
                    }.run {
                        HeadhuntingFragment.adapter!!.notifyDataSetChanged()
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
                                conferDataSet.add(
                                    ConferenceInfo(
                                        uploader =  document.data["uploader"] as String,     //0
                                        title =  document.data["title"] as String,        //1
                                        date =  document.data["date"] as String,         //2
                                        price =  document.data["price"] as Long,           //3
                                        offline = document.data["offline"] as Boolean,     //4
                                        conferenceURL = document.data["conferenceURL"] as String,//5
                                        content = document.data["content"] as String,       //6
                                        uid = document.data["uid"] as String,
                                        documentID =  document.data["documentID"] as String,
                                        image = document.data["image"] as MutableList<Uri>,
                                        startDate = document.data["startDate"] as String,   //10
                                        finishDate = document.data["finishDate"] as String,   //11
                                        place = document.data["place"] as String, //12
                                        manager = document.data["manager"] as Boolean
                                    )
                                )
                            }
                        }.run {
                            HomeFragment.adapterConference!!.notifyDataSetChanged()
                        }
                    }
                }
                DBType.STUDY->{
                    FirebaseIO.readPublic("groupstudyDocument").addOnSuccessListener { result ->
                        run {
                            studyDataSet.clear()
                            for (document in result) {
                                studyDataSet.add(
                                    StudyInfo(
                                        ongoing = document.data["ongoing"] as Boolean,
                                        uploader = document.data["uploader"] as String,
                                        title = document.data["title"] as String,
                                        content = document.data["content"] as String,
                                        offline = document.data["offline"] as Boolean,
                                        studyURL = document.data["studyURL"] as String,
                                        totalMember =  document.data["totalMember"] as Long,
                                        remainingMemeber = document.data["remainingMemeber"] as Long,
                                        language = document.data["language"] as MutableList<String>,
                                        documentID = document.data["documentID"] as String,
                                        uid = document.data["uid"] as String,
                                        endDate = document.data["endDate"] as String
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
                DBType.HEADHUNTING -> {
                    headhuntingDataSet.clear()
                }
            }

        }

        fun updateUserInfo(){
            FirebaseFirestore.getInstance().collection("UserInfo")
                .whereEqualTo("email", FirebaseAuth.getInstance().currentUser?.email)
                .get()
                .addOnSuccessListener {
                    for(document in it){
                        userInfo.id = document["id"] as String
                        //userInfo.position = document["position"] as Long
                        try{
                            userInfo.language = document["language"] as MutableList<String>
                        }catch (e : Exception){
                            userInfo.language = emptyList<String>().toMutableList()
                        }

                    }
                }
                .addOnFailureListener{
                    Log.d("TAG", "findUploader: ${it.stackTrace}")
                }
        }

    }

}