package com.example.devproject.util

import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.activity.conference.AddConferencesActivity
import com.example.devproject.others.ListAdapter
import com.example.devproject.R
import com.example.devproject.activity.MainActivity

class UIHandler {

    companion object{
        private var conferAddButton : Button? = null// findViewById<Button>(R.id.addBtn)
        var adapter : ListAdapter? = null
        var rootView : View? = null
        var mainActivity : MainActivity? = null
        var languageNumberTextView : TextView? = null

        fun allocateUI(rootView: View, mainActivity: MainActivity) {
            Companion.rootView = rootView
            Companion.mainActivity = mainActivity
            conferAddButton = rootView.findViewById(R.id.conferAddButton)
        }
    }
}