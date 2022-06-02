package com.example.devproject.adapter

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.devproject.R
import com.example.devproject.activity.conference.ShowConferenceDetailActivity
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
class ListAdapter() : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var context : Context? = null
    private var todayDate = DataHandler.currentDate.toInt()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conferLinkImageView : ImageView = view.findViewById(R.id.conferLinkImageView)
        val conferPreTitleTextVIew : TextView = view.findViewById(R.id.conferPreTitleTextView)
        val conferPreStartDateTextView : TextView = view.findViewById(R.id.conferPreStartDateTextView)
        val conferPreFinishDateTextView : TextView = view.findViewById(R.id.conferPreFinishDateTextView)
        val conferPreContentTextView : TextView = view.findViewById(R.id.conferPreContentTextView)
        val conferPreCardView: CardView = view.findViewById(R.id.conferPreCardView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.conference_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.conferLinkImageView.setOnClickListener {
            val intent = Intent(context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", DataHandler.conferDataSet[position][5].toString())
            context?.startActivity(intent)
        }
        viewHolder.conferPreTitleTextVIew.text = DataHandler.conferDataSet[position][1].toString()
        var startDate = DataHandler.conferDataSet[position][10].toString()
        var endDate = DataHandler.conferDataSet[position][11].toString()

        val startDateInt = startDate.replace(". ", "").toInt()
        val endDateInt = endDate.replace(". ", "").toInt()
        if(todayDate in startDateInt..endDateInt){
            viewHolder.conferPreTitleTextVIew.setTextColor(Color.WHITE)
            viewHolder.conferPreTitleTextVIew.setBackgroundColor(Color.rgb(153, 204, 0))
        }else{
            viewHolder.conferPreTitleTextVIew.setTextColor(Color.BLACK)
            viewHolder.conferPreTitleTextVIew.setBackgroundColor(Color.WHITE)
        }

        viewHolder.conferPreStartDateTextView.text = startDate.substring(2, startDate.length)
        viewHolder.conferPreFinishDateTextView.text =endDate.substring(2, endDate.length)
        viewHolder.conferPreContentTextView.text = DataHandler.conferDataSet[position][6].toString()
        viewHolder.conferPreCardView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowConferenceDetailActivity::class.java)
            intent.putExtra("position", position)
            UIHandler.rootView?.context?.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
        }
        viewHolder.conferPreCardView.setOnLongClickListener {
            true
        }

    }

    override fun getItemCount() = DataHandler.conferDataSet.size
}