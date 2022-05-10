package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.ShowConferenceDetailActivity
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var context : Context? = null
    private var todayDate = DataHandler.currentDate.toInt()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conferPreImageView : ImageView = view.findViewById(R.id.conferPreImageView)
        val conferPreImageView2 : ImageView = view.findViewById(R.id.imageView2)
        val conferPreTitleTextVIew : TextView = view.findViewById(R.id.conferPreTitleTextView)
        val conferPreDateTextView : TextView = view.findViewById(R.id.conferPreDateTextView)
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
        //Binding Image & Text data Set trough firebase
        viewHolder.conferPreImageView.setImageResource(R.drawable.default_image)
        viewHolder.conferPreImageView2.visibility = View.GONE
        if(todayDate > DataHandler.conferDataSet[position][2].toString().replace(". ", "").toInt()){
            viewHolder.conferPreImageView2.visibility = View.VISIBLE
        }

        viewHolder.conferPreImageView.setOnClickListener {
            val intent = Intent(context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", DataHandler.conferDataSet[position][5].toString())
            context?.startActivity(intent)
        }
        viewHolder.conferPreTitleTextVIew.text = DataHandler.conferDataSet[position][1].toString()
        viewHolder.conferPreDateTextView.text = DataHandler.conferDataSet[position][2].toString()// date
        viewHolder.conferPreContentTextView.text = DataHandler.conferDataSet[position][6].toString()

        viewHolder.conferPreCardView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowConferenceDetailActivity::class.java)
            intent.putExtra("position", position)
            UIHandler.rootView?.context?.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))

        }
        viewHolder.conferPreCardView.setOnLongClickListener {
            //TO DO Somethinmg
            true
        }

    }

    override fun getItemCount() = DataHandler.conferDataSet.size
}