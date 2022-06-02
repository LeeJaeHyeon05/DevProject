package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devproject.R
import com.example.devproject.activity.conference.ShowDetailImage

class ImageSliderAdapter(private val imageList: ArrayList<Uri> = ArrayList<Uri>(), private val context: Context, private val count: Int): RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageUri: ImageView = view.findViewById(R.id.conferenceDetailItemImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.conference_detail_list_item, parent, false))

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(imageList[position])
            .into(holder.imageUri)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ShowDetailImage::class.java)
            intent.putExtra("image", imageList[position].toString())
            it.context.startActivity(intent)
        }
    }
}