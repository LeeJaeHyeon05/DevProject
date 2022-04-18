package com.example.devproject

import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UIHandler {

    companion object{
        private var conferAddButton : Button? = null// findViewById<Button>(R.id.addBtn)
        private var conferRecyclerView : RecyclerView? = null//findViewById<RecyclerView>(R.id.conferRecyclerView)
        private var adapter : ListAdapter? = null// ListAdapter(emptyList<Array<File>>().toMutableList(), dummyTextDataSet)
        var rootView : View? = null

        fun allocateUI(rootView: View) {
            this.rootView = rootView

            this.conferAddButton = rootView.findViewById(R.id.conferAddButton)
            this.conferAddButton?.setOnClickListener {
                //go to 글쓰기 페이지
            }

            this.conferRecyclerView = rootView.findViewById(R.id.conferRecyclerView)
            this.adapter = ListAdapter(DataHandler.imageDataSet, DataHandler.textDataSet)
        }

        fun activateUI(id: Int) {
            when (id) {
                R.id.conferRecyclerView ->{
                    this.conferRecyclerView?.layoutManager = LinearLayoutManager(rootView?.context)
                    this.conferRecyclerView?.adapter = adapter
                }
                else ->{

                }
            }
        }

    }
}