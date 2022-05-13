package com.example.devproject.addConferences

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.devproject.R
import com.example.devproject.activity.ShowConferenceDetailActivity
import com.example.devproject.addConferences.ImageViewAdapter.ViewHolder
import com.example.devproject.databinding.DialogShowImageBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO.Companion.storage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageViewAdapter(private val imageList: ArrayList<Uri>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {
    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        var addConferenceImageView: ImageView? = null
        var conferDetailImageView: ImageView = view.findViewById(R.id.conferenceDetailItemImage)
        init {
            when(view.context){
                is AddConferencesActivity -> {
                    view.setOnClickListener {
                        val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(view.context))

                        var builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
                        var ad = builder.create()

                        ad.setView(dialogBinding.root)
                        dialogBinding.showImageView.setImageDrawable(addConferenceImageView?.drawable)
                    }
                }

                is ShowConferenceDetailActivity -> {

                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.add_conference_imagelist_item, parent, false)
        when(parent.context){
            is ShowConferenceDetailActivity -> {
               view = LayoutInflater.from(parent.context).inflate(R.layout.conference_detail_list_item, parent, false)
            }
        }
        return ViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(holder.itemView.context){
            is AddConferencesActivity -> {
                val item = imageList[position]
                holder.addConferenceImageView?.let {
                    Glide.with(context)
                        .load(item)
                        .override(SIZE_ORIGINAL)
                        .into(it)
                }
                holder.addConferenceImageView?.setOnClickListener {
                    val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(context))
                    val builder = AlertDialog.Builder(context)
                    val ad = builder.create()

                    ad.setView(dialogBinding.root)
                    ad.show()

                    dialogBinding.showImageView.setImageURI(imageList[position])
                    ad.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    dialogBinding.dialogShowImageOkButton.setOnClickListener {
                        ad.dismiss()
                    }

                    dialogBinding.dialogShowImageDeleteBtn.setOnClickListener {
                        imageList.removeAt(position)
                        notifyDataSetChanged()
                        ad.dismiss()
                    }
                }
            }

            is ShowConferenceDetailActivity -> {
                Glide.with(holder.itemView.context)
                    .load(imageList[position])
                    .fitCenter()
                    .into(holder.conferDetailImageView)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}