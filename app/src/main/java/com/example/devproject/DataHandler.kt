package com.example.devproject

import java.io.File

class DataHandler {
    companion object {

        lateinit var imageDataSet : MutableList<Array<File>>
        lateinit var textDataSet : MutableList<Array<String>>

        fun load() {

            imageDataSet = emptyList<Array<File>>().toMutableList()

            //for test.
            val dummyTextDataSet = emptyList<Array<String>>().toMutableList()
            dummyTextDataSet.add(arrayOf("네이버", "1월 1일", "네이버 무엇"))
            dummyTextDataSet.add(arrayOf("카카오", "1월 2일", "카카오 초콜릿"))
            dummyTextDataSet.add(arrayOf("라인", "1월 3일", "라인 잘 타라"))
            dummyTextDataSet.add(arrayOf("쿠팡", "1월 4일", "쿠팡 무엇"))
            dummyTextDataSet.add(arrayOf("배민", "1월 5일", "배달료 6000원 실화임???"))

            textDataSet = dummyTextDataSet
        }
    }

}