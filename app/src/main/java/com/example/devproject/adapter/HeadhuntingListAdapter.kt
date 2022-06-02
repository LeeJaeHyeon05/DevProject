package com.example.devproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.util.DataHandler

class HeadhuntingListAdapter : RecyclerView.Adapter<HeadhuntingListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HeadhuntingListAdapter.ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.headhunting_list_item, parent, false))

    override fun onBindViewHolder(holder: HeadhuntingListAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = DataHandler.headhuntingDataSet.size
}