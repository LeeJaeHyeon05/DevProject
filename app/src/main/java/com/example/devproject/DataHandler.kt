package com.example.devproject

import java.io.File

class DataHandler {
    companion object {

        lateinit var imageDataSet : MutableList<Array<File>>
        var textDataSet : MutableList<Array<String>> = emptyList<Array<String>>().toMutableList()

        //firebase load
        fun load() {
            imageDataSet = emptyList<Array<File>>().toMutableList()

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
           textDataSet = emptyList<Array<String>>().toMutableList()
        }
    }

}