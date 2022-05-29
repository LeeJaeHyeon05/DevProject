package com.example.devproject.others

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.util.UIHandler

class LanguageListAdapter(languageArray: TypedArray , languages: MutableList<String>?) : RecyclerView.Adapter<LanguageListAdapter.ViewHolder>() {

    private var languageArray = languageArray
    private var context : Context? = null
    var languageMap = LinkedHashMap<String, Boolean>()
    var arr : MutableList<Boolean> = emptyList<Boolean>().toMutableList()

    init{
        languageMap["csharp"] = false
        languageMap["cpp"] = false
        languageMap["kotlin"] = false
        languageMap["javascript"] = false
        languageMap["swift"] = false
        languageMap["go"] = false

        languages?.forEach {
            languageMap[it] = true
        }

        languageMap.forEach {
            arr.add(it.value)
        }

        println(languages)
        println(arr)
        UIHandler.languageNumberTextView?.text = updateLanguageSelectedNumber().toString()
    }

    fun getLanguageMaps() : HashMap<String, Boolean> {
            arr.forEachIndexed { index, b ->
                if(b){
                    when(index){
                        0 -> languageMap["csharp"] = true
                        1 -> languageMap["cpp"] = true
                        2 -> languageMap["kotlin"] = true
                        3 -> languageMap["javascript"] = true
                        4 -> languageMap["swift"] = true
                        5 -> languageMap["go"] = true
                    }
                }
            }
            return languageMap
        }

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
        viewHolder.languageImageView.setImageDrawable(languageArray.getDrawable(position))
        if(arr[position]){
            viewHolder.languageImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        }

        viewHolder.languageImageView.setOnClickListener {
            if(!arr[position]){
                viewHolder.languageImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
                arr[position] = true
            }else{
                viewHolder.languageImageView.clearColorFilter()
                arr[position] = false
            }
            UIHandler.languageNumberTextView?.text = updateLanguageSelectedNumber().toString()
        }
    }


    override fun getItemCount() = languageArray.length()

    private fun updateLanguageSelectedNumber() : Int {
        var count = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           arr.forEach { if(it) {
               count++
            }
           }
        }
        return count
    }
}