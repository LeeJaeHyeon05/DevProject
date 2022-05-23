package com.example.devproject.others

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.devproject.R
import com.example.devproject.activity.conference.AddConferencesActivity
import com.example.devproject.activity.conference.EditConferenceActivity
import com.example.devproject.activity.conference.ShowConferenceDetailActivity
import com.example.devproject.others.ImageViewAdapter.ViewHolder
import com.example.devproject.databinding.DialogShowImageBinding
import com.example.devproject.util.FirebaseIO

class ImageViewAdapter(private val imageList: ArrayList<Uri> = ArrayList<Uri>(), private val context: Context, private val deleteImageList: ArrayList<Uri> = ArrayList<Uri>()): RecyclerView.Adapter<ViewHolder>() {

    private var deleteReturnList = ArrayList<Uri>()

    inner class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        var addConferenceImageView: ImageView? = null
        var conferDetailImageView: ImageView? = null
        init {
            when(view.context){
                is AddConferencesActivity -> {
                    addConferenceImageView = view.findViewById(R.id.IvAddConferenceListImageView)
                    view.setOnClickListener {
                        val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(view.context))

                        var builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
                        var ad = builder.create()

                        ad.setView(dialogBinding.root)
                        dialogBinding.showImageView.setImageDrawable(addConferenceImageView?.drawable)
                    }
                }

                is ShowConferenceDetailActivity -> {
                    conferDetailImageView = view.findViewById(R.id.conferenceDetailItemImage)
                }

                is EditConferenceActivity -> {
                    addConferenceImageView = view.findViewById(R.id.IvAddConferenceListImageView)
                    view.setOnClickListener {
                        val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(view.context))

                        var builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
                        var ad = builder.create()

                        ad.setView(dialogBinding.root)
                        dialogBinding.showImageView.setImageDrawable(addConferenceImageView?.drawable)
                    }
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
                holder.conferDetailImageView?.let {
                    Glide.with(holder.itemView.context)
                        .load(imageList[position])
                        .fitCenter()
                        .into(it)
                }
            }

            is EditConferenceActivity -> {
                val item = imageList[position]
                holder.addConferenceImageView?.let {
                    Glide.with(context)
                        .load(item)
                        .override(SIZE_ORIGINAL)
                        .into(it)
                }
                holder.addConferenceImageView?.setOnClickListener {
                    val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(it.context))
                    val builder = AlertDialog.Builder(it.context)
                    val ad = builder.create()

                    ad.setView(dialogBinding.root)
                    ad.show()

                    dialogBinding.showImageView.let { image ->
                        Glide.with(context)
                            .load(imageList[position])
                            .override(SIZE_ORIGINAL)
                            .into(image)
                    }

                    ad.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    dialogBinding.dialogShowImageOkButton.setOnClickListener {
                        ad.dismiss()
                    }

                    dialogBinding.dialogShowImageDeleteBtn.setOnClickListener {
                        if(deleteImageList.isEmpty()){
                            imageList.removeAt(position)
                            notifyDataSetChanged()
                            ad.dismiss()
                        }
                        else{
                            deleteReturnList.add(deleteImageList[position])
                            imageList.removeAt(position)
                            deleteImageList.removeAt(position)
                            notifyDataSetChanged()
                            ad.dismiss()
                        }
                    }
                }
            }
        }
    }

    fun getDeleteImage(): ArrayList<Uri> {
        return deleteReturnList
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}