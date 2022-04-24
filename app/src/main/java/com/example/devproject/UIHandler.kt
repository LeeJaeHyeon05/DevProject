package com.example.devproject

import android.content.Intent
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UIHandler {

    companion object{
        private var conferAddButton : Button? = null// findViewById<Button>(R.id.addBtn)
        private var conferRecyclerView : RecyclerView? = null//findViewById<RecyclerView>(R.id.conferRecyclerView)
        private var adapter : ListAdapter? = null// ListAdapter(emptyList<Array<File>>().toMutableList(), dummyTextDataSet)
        var rootView : View? = null
        var mainActivity : MainActivity? = null

        fun allocateUI(rootView: View, mainActivity : MainActivity) {
            this.rootView = rootView
            this.mainActivity = mainActivity
            this.conferAddButton = rootView.findViewById(R.id.conferAddButton)
            this.conferRecyclerView = rootView.findViewById(R.id.conferRecyclerView)
            this.adapter = ListAdapter(DataHandler.imageDataSet, DataHandler.textDataSet)
        }

        fun activateUI(id: Int) {
            when (id) {
                R.id.conferRecyclerView ->{
                    this.conferRecyclerView?.layoutManager = LinearLayoutManager(rootView?.context)
                    this.conferRecyclerView?.adapter = adapter
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