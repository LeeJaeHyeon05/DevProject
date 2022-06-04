package com.example.devproject.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.others.PositionType
import com.example.devproject.util.DataHandler.Companion.headhuntingDataSet
import com.example.devproject.util.UIHandler

class HeadhuntingListAdapter : RecyclerView.Adapter<HeadhuntingListAdapter.ViewHolder>() {

    private var context : Context? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var headhuntingPositionTextView : TextView = view.findViewById(R.id.headhuntingPositionTextView)
        var headhuntingEmailImageButton : ImageButton = view.findViewById(R.id.headhuntingEmailImageButton)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.headhunting_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.headhuntingPositionTextView.text = convertIntToPosition(headhuntingDataSet[position][0].toString().toInt())

        viewHolder.headhuntingEmailImageButton.setOnClickListener {
            var email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf(headhuntingDataSet[position][1].toString())
            email.putExtra(Intent.EXTRA_EMAIL, address)
            UIHandler.mainActivity!!.startActivity(email)
        }
    }

    override fun getItemCount(): Int = headhuntingDataSet.size

    private fun convertIntToPosition(index : Int) : String{
        return when(index) {
            0 -> PositionType.FRONTEND.krName
            1 -> PositionType.BACKEND.krName
            2 -> PositionType.FULLSTACK.krName
            3 -> PositionType.EMBEDED.krName
            4 -> PositionType.PUBLISHER.krName
            else -> PositionType.FRONTEND.krName
        }
    }

}