package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.ShowConferenceDetailActivity
import com.example.devproject.util.UIHandler
import java.io.File

class ListAdapter(val imageDataSet : MutableList<Array<File>>,
                  val conferDataSet : MutableList<Array<Any>>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var context : Context? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conferPreImageView : ImageView = view.findViewById(R.id.conferPreImageView)
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
        viewHolder.conferPreImageView.setImageResource(R.drawable.ic_launcher_foreground)
        viewHolder.conferPreTitleTextVIew.text = conferDataSet[position][1].toString()
        viewHolder.conferPreDateTextView.text = conferDataSet[position][2].toString()
        viewHolder.conferPreContentTextView.text = conferDataSet[position][6].toString()
        viewHolder.conferPreCardView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowConferenceDetailActivity::class.java)
            intent.putExtra("position", position)
            UIHandler.rootView?.context?.startActivity(intent)

        }
        viewHolder.conferPreCardView.setOnLongClickListener {
            //TO DO Somethinmg
            true
        }
    }

    override fun getItemCount() = conferDataSet.size
}