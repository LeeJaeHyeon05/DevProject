package com.example.devproject.adapter

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler

class PositionListAdapter(positionArray: TypedArray, positions: MutableList<String>?) : RecyclerView.Adapter<PositionListAdapter.ViewHolder>() {
    private var positionArray = positionArray
    private var context : Context? = null
    private var positionMap = LinkedHashMap<String, Boolean>()
    private var selected = false;
    private var selectedPosition = ""
    init{
        positionMap["frontend"] = false
        positionMap["backend"] = false
        positionMap["fullstack"] = false
        positionMap["embeded"] = false
        positionMap["publisher"] = false
        positions?.forEach {
            positionMap[it] = true
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var positionSelectImageView : ImageView = view.findViewById(R.id.positionSelectImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.position_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.positionSelectImageView.setImageDrawable(positionArray.getDrawable(position))
        if(positionMap[convertIndexToKey(position)] == true){
            viewHolder.positionSelectImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        }

        viewHolder.positionSelectImageView.setOnClickListener {
            if(positionMap[convertIndexToKey(position)] == false && !selected){
                viewHolder.positionSelectImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
                positionMap[convertIndexToKey(position)] = true
                selected = true
                selectedPosition = convertIndexToKey(position)
                UIHandler.positionTextView?.text = selectedPosition
            }else if(positionMap[convertIndexToKey(position)] == true && selected){
                viewHolder.positionSelectImageView.clearColorFilter()
                positionMap[convertIndexToKey(position)] = false
                selected = false
                selectedPosition = ""
                UIHandler.positionTextView?.text = selectedPosition
            }else{
                viewHolder.positionSelectImageView.clearColorFilter()
                positionMap[convertIndexToKey(position)] = false
            }
        }
    }

    private fun convertIndexToKey(position : Int) : String {
        return  when(position){
            0-> "frontend"
            1-> "backend"
            2-> "fullstack"
            3-> "embeded"
            4-> "publisher"
            else ->  "frontend"
        }
    }

    fun convertKeyToIndex() : Int{
        return  when(selectedPosition){
            "frontend" -> 0
            "backend" -> 1
            "fullstack" -> 2
            "embeded" -> 3
            "publisher" -> 4
            else -> 0
        }
    }


    override fun getItemCount(): Int = positionArray.length()
}