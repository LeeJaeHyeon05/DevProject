package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.devproject.R
import com.example.devproject.activity.conference.ShowConferenceDetailActivity
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.language_list_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LanguageListAdapter(languageArray: TypedArray) : RecyclerView.Adapter<LanguageListAdapter.ViewHolder>() {

    var languageArray = languageArray
    var arr : MutableList<Boolean> = listOf(false, false, false, false).toMutableList()

    private var context : Context? = null
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var languageImageView : ImageView = view.findViewById(R.id.languageImageView)
//        var languageNumberTextView : TextView = view.findViewById(R.id.languageNumberTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.language_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.languageImageView.setImageDrawable(languageArray.getDrawable(position))
        viewHolder.languageImageView.setOnClickListener {
            if(!arr[position]){
                viewHolder.languageImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
                arr[position] = true
            }else{
                viewHolder.languageImageView.clearColorFilter()
                arr[position] = false
            }

//            viewHolder.languageNumberTextView.text = updateLanguageSelectedNumber().toString()

        }
    }


    override fun getItemCount() = languageArray.length()

    fun updateLanguageSelectedNumber() : Int {
        var count : Int = 0
        arr.forEach { if(it) count++ }
        return count
    }
}