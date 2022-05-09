package com.example.devproject.addConferences

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devproject.R
import com.example.devproject.addConferences.ImageViewAdapter.ViewHolder

class ImageViewAdapter(private val imageList: ArrayList<Uri>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val addConferenceImageView = view.findViewById<ImageView>(R.id.IvAddConferenceListImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_conference_imagelist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList[position]
        Glide.with(context)
            .load(item)
            .into(holder.addConferenceImageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}