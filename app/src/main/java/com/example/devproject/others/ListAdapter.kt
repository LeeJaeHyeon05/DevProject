package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var context : Context? = null
    private var todayDate = DataHandler.currentDate.toInt()
    private var storage = FirebaseStorage.getInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conferPreImageView : ImageView = view.findViewById(R.id.conferPreImageView)
        val conferPreImageView2 : ImageView = view.findViewById(R.id.imageView2)
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
        //Binding Image & Text data Set trough firebase
        //이미지가 들어가야함
        getImage(viewHolder, position)
        viewHolder.conferPreImageView.setImageResource(R.drawable.ic_main)
        viewHolder.conferPreImageView2.visibility = View.GONE
        if(todayDate > DataHandler.conferDataSet[position][11].toString().replace(". ", "").toInt()){
            viewHolder.conferPreImageView2.visibility = View.VISIBLE
        }

        viewHolder.conferPreImageView.setOnClickListener {
            val intent = Intent(context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", DataHandler.conferDataSet[position][5].toString())
            context?.startActivity(intent)
        }
        viewHolder.conferPreTitleTextVIew.text = DataHandler.conferDataSet[position][1].toString()
        viewHolder.conferPreStartDateTextView.text = DataHandler.conferDataSet[position][10].toString()
        viewHolder.conferPreFinishDateTextView.text = DataHandler.conferDataSet[position][11].toString()
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

    private fun getImage(viewHolder: ViewHolder, position: Int): Boolean {
        var success = false
        CoroutineScope(Dispatchers.Main).launch {
            val list: MutableList<*> = DataHandler.conferDataSet[position][9] as MutableList<*>
            if(list.isNotEmpty()){
                val path = list[0].toString()
                val storageRef = storage.reference.child(path)

                storageRef.downloadUrl.addOnSuccessListener {
                    Glide.with(viewHolder.itemView.context)
                        .load(it)
                        .fitCenter()
                        .into(viewHolder.conferPreImageView)
                }
            }
        }

        if(viewHolder.conferPreImageView.drawable != null){
            success = true
        }
        return success
    }

    override fun getItemCount() = DataHandler.conferDataSet.size
}