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
        UIHandler.languageNumberTextView?.text = updateLanguageSelectedNumber().toString()
    }

    fun getLanguageList() : MutableList<String> {
        println("what the hell?")
        var languageList : MutableList<String> = emptyList<String>().toMutableList()
        languageMap.forEach { if(it.value){
            languageList.add(it.key)
            println(it.key)
        }
        }
        return languageList
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

        if(languageMap[convertIndexToKey(position)] == true){
            viewHolder.languageImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
        }

        viewHolder.languageImageView.setOnClickListener {
            if(languageMap[convertIndexToKey(position)] == false){
                viewHolder.languageImageView.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY)
                languageMap[convertIndexToKey(position)] = true
            }else{
                viewHolder.languageImageView.clearColorFilter()
                languageMap[convertIndexToKey(position)] = false
            }
            UIHandler.languageNumberTextView?.text = updateLanguageSelectedNumber().toString()
        }
    }


    override fun getItemCount() = languageArray.length()

    private fun convertIndexToKey(position : Int) : String {
        return  when(position){
            0 -> "csharp"
            1->"cpp"
            2->"kotlin"
            3->"javascript"
            4->"swift"
            5->"go"
            else ->  "kotlin"
        }
    }

    private fun updateLanguageSelectedNumber() : Int {
        var count = 0
        languageMap.forEach {
            if(it.value){
                count++;
            }
        }
        return count
    }
}