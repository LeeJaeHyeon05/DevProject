package com.example.devproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListAdapter(val imageDataSet : MutableList<Array<File>>,
                  val textDataSet : MutableList<Array<String>>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var context : Context? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conferImageView : ImageView = view.findViewById(R.id.conferImageView)
        val conferTitleTextVIew : TextView = view.findViewById(R.id.conferTitleTextView)
        val conferDateTextView : TextView = view.findViewById(R.id.conferDateTextView)
        val conferContentTextView : TextView = view.findViewById(R.id.conferContentTextView)
        val conferCardView: CardView = view.findViewById(R.id.conferCardView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.conference_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //Binding Image & Text data Set trough firebase
        viewHolder.conferImageView.setImageResource(R.drawable.ic_launcher_foreground)
        viewHolder.conferTitleTextVIew.text = textDataSet[position][0]
        viewHolder.conferDateTextView.text = textDataSet[position][1]
        viewHolder.conferContentTextView.text = textDataSet[position][2]
        viewHolder.conferCardView.setOnLongClickListener {
            //TO DO Somethinmg
            true
        }
    }

    override fun getItemCount() = textDataSet.size
}