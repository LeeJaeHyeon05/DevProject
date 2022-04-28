package com.example.devproject.util

import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.addConferences.AddConferencesActivity
import com.example.devproject.others.ListAdapter
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
class UIHandler {

    companion object{
        private var conferAddButton : Button? = null// findViewById<Button>(R.id.addBtn)
        private var conferRecyclerView : RecyclerView? = null//findViewById<RecyclerView>(R.id.conferRecyclerView)
        private var adapter : ListAdapter? = null// ListAdapter(emptyList<Array<File>>().toMutableList(), dummyTextDataSet)
        var rootView : View? = null
        var mainActivity : MainActivity? = null

        fun allocateUI(rootView: View, mainActivity: MainActivity) {
            Companion.rootView = rootView
            Companion.mainActivity = mainActivity
            conferAddButton = rootView.findViewById(R.id.conferAddButton)
            conferRecyclerView = rootView.findViewById(R.id.conferRecyclerView)
            adapter = ListAdapter(DataHandler.imageDataSet, DataHandler.textDataSet)
        }

        fun activateUI(id: Int) {
            when (id) {
                R.id.conferRecyclerView ->{
                    conferRecyclerView?.layoutManager = LinearLayoutManager(rootView?.context)
                    conferRecyclerView?.adapter = adapter
                }
                R.id.conferAddButton ->{
                    conferAddButton?.setOnClickListener {
                        mainActivity?.startActivity(Intent(rootView?.context, AddConferencesActivity::class.java))
                    }
                }
                else ->{

                }
            }
        }

    }
}