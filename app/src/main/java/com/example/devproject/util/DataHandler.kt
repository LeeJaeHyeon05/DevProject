package com.example.devproject.util

import java.io.File

class DataHandler {
    companion object {

        var imageDataSet : MutableList<Array<File>> = emptyList<Array<File>>().toMutableList()
        var textDataSet : MutableList<Array<String>> = emptyList<Array<String>>().toMutableList()

        //firebase load
        fun load() {
            FirebaseIO.read("conferenceDocument").addOnSuccessListener { result ->
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

        fun delete(){
            imageDataSet = emptyList<Array<File>>().toMutableList()
            textDataSet = emptyList<Array<String>>().toMutableList()
        }
    }

}