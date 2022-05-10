package com.example.devproject.addConferences

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.example.devproject.R
import com.example.devproject.addConferences.ImageViewAdapter.ViewHolder
import com.example.devproject.databinding.DialogShowImageBinding

class ImageViewAdapter(private val imageList: ArrayList<Uri>, private val context: Context): RecyclerView.Adapter<ViewHolder>() {
    class ViewHolder (view: View): RecyclerView.ViewHolder(view){
        val addConferenceImageView: ImageView = view.findViewById<ImageView>(R.id.IvAddConferenceListImageView)

        init {
            view.setOnClickListener {
                val dialogBinding: DialogShowImageBinding = DialogShowImageBinding.inflate(LayoutInflater.from(view.context))

                var builder = androidx.appcompat.app.AlertDialog.Builder(view.context)
                var ad = builder.create()

                ad.setView(dialogBinding.root)
                dialogBinding.showImageView.setImageDrawable(addConferenceImageView.drawable)
                ad.setTitle("비밀번호 찾기")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_conference_imagelist_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageList[position]
        Glide.with(context)
            .load(item)
            .override(SIZE_ORIGINAL)
            .into(holder.addConferenceImageView)
        holder.addConferenceImageView.setOnClickListener {
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

    override fun getItemCount(): Int {
        return imageList.size
    }

}