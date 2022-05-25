package com.example.devproject.others

import android.content.Context
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.util.UIHandler

class LanguageListAdapter2(languageArray: MutableList<String>) : RecyclerView.Adapter<LanguageListAdapter2.ViewHolder>() {

    var languageArray = languageArray

    private var context : Context? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var languageImageView : ImageView = view.findViewById(R.id.languageImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.language_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.languageImageView.layoutParams.width = 100;
        viewHolder.languageImageView.layoutParams.height = 100;
        viewHolder.languageImageView.requestLayout()

        when(languageArray[position]){
            "csharp" -> viewHolder.languageImageView.setImageResource(R.drawable.logo_csharp)
            "cpp" -> viewHolder.languageImageView.setImageResource(R.drawable.logo_cplusplus)
            "kotlin" -> viewHolder.languageImageView.setImageResource(R.drawable.logo_kotlin)
            "javascript" -> viewHolder.languageImageView.setImageResource(R.drawable.logo_javascript)
            "swift" -> viewHolder.languageImageView.setImageResource(R.drawable.logo_swift)
        }
    }


    override fun getItemCount() = languageArray.size
}