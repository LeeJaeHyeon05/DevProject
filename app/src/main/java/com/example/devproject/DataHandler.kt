package com.example.devproject

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class DataHandler {
    companion object {

        lateinit var imageDataSet : MutableList<Array<File>>
        var textDataSet : MutableList<Array<String>> = emptyList<Array<String>>().toMutableList()
        private var db = FirebaseFirestore.getInstance()

        //firebase load
        fun load() {
            imageDataSet = emptyList<Array<File>>().toMutableList()
            db.collection("conferenceDocument").get().addOnSuccessListener { result ->
                run {
                    for (document in result) {
                        textDataSet.add(arrayOf(
                                document.data["title"] as String,
                                "1월1일",
                                document.data["content"] as String
                            )
                        )
                    }
                }
            }
        }
    }

}