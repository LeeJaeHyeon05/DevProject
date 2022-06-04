package com.example.devproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.util.DataHandler

class HeadhuntingListAdapter : RecyclerView.Adapter<HeadhuntingListAdapter.ViewHolder>() {

    private var context : Context? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var headhuntingPositionTextView : TextView = view.findViewById(R.id.headhuntingPositionTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.headhunting_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.headhuntingPositionTextView.text = DataHandler.headhuntingDataSet[position][0].toString()
    }

    override fun getItemCount(): Int = DataHandler.headhuntingDataSet.size
}