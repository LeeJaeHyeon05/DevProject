package com.example.devproject.util

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.adapter.ListAdapter
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.example.devproject.activity.conference.ImageCounterViewModel
import com.example.devproject.adapter.ImageViewAdapter

class UIHandler {



    companion object{
        private var conferAddButton : Button? = null// findViewById<Button>(R.id.addBtn)
        var adapter : ListAdapter? = null
        var rootView : View? = null
        var mainActivity : MainActivity? = null
        var languageNumberTextView : TextView? = null
        lateinit var viewModel: ImageCounterViewModel
        fun allocateUI(rootView: View, mainActivity: MainActivity) {
            Companion.rootView = rootView
            Companion.mainActivity = mainActivity
            conferAddButton = rootView.findViewById(R.id.conferAddButton)
        }

        fun countImage(result: ActivityResult, list: ArrayList<Uri>, context: Context, view: RecyclerView, adapter: ImageViewAdapter, viewModel: ImageCounterViewModel): Int{

            if(result.data != null){
                val imageData = result.data
                var size = imageData?.clipData?.itemCount

                var adapter = adapter

                if(size != null){
                    if(list.size < 4){
                        val imageUri = imageData?.clipData
                        if (imageUri != null) {
                            if(list.size + size < 4){ // imagelist는 사진을 추가할때마다 값이 커짐, size는 추가한 사진의 개수만 가져옴, 둘이합쳐서 3개이면 다 넣어도됨
                                for(i in 0 until size){
                                    list.add(result.data!!.clipData!!.getItemAt(i).uri)
                                }
                            }
                            else{ //기존 선택한 사진과 새로 추가한 사진의 개수가 총 3개가 넘으면 기존선택한 사진의 개수는 유지하고 새로추가한 사진의 개수를 빼야함
                                var index = 0
                                while(list.size != 3){
                                    list.add(result.data!!.clipData!!.getItemAt(index).uri)
                                    index++
                                }
                                Toast.makeText(context, "이미지는 3개까지 선택가능합니다", Toast.LENGTH_SHORT).show()
                            }
                            list.sortDescending()
                            adapter = ImageViewAdapter(imageList = list, context, viewModel = viewModel)
                            view.adapter = adapter
                            view.layoutManager =  LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

                        }
                    }
                }
            }
            return list.size
        }
    }


}