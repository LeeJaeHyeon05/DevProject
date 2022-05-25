package com.example.devproject.others

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.fragment.StudyFragment
import com.example.devproject.util.DataHandler.Companion.studyDataSet
import com.example.devproject.util.UIHandler


class StudyListAdapter() : RecyclerView.Adapter<StudyListAdapter.ViewHolder>() {

    private var context : Context? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val studyPreOngoingTextView : TextView = view.findViewById(R.id.studyPreOngoingTextView)
        val studyPreTitleTextView : TextView = view.findViewById(R.id.studyPreTitleTextView)
        val studyOfflineTextView : TextView= view.findViewById(R.id.studyOfflineTextView)
        val studyLinkImageView : ImageView= view.findViewById(R.id.studyLinkImageView)
        val remainingMemeberTextView :TextView = view.findViewById(R.id.remainingMemberTextView)
        val languageRecyclerView2 : RecyclerView = view.findViewById(R.id.languageRecyclerView2)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.study_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //Binding Image & Text data Set trough firebase
        if(studyDataSet.size == 0 ) return
        viewHolder.studyPreOngoingTextView.text = if(studyDataSet[position][0] as Boolean) "모집중" else "마감"
        viewHolder.studyPreTitleTextView.text = studyDataSet[position][2].toString()
        viewHolder.studyOfflineTextView.text = if(studyDataSet[position][4] as Boolean) "온라인" else "오프라인"
        viewHolder.studyLinkImageView.setImageResource(R.drawable.link)
        viewHolder.studyLinkImageView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", studyDataSet[position][4].toString())
            UIHandler.rootView?.context?.startActivity(intent)
        }
        viewHolder.remainingMemeberTextView.text = studyDataSet[position][7].toString() +"명 남음!"
        var typedArray : TypedArray = UIHandler.rootView?.resources!!.obtainTypedArray(R.array.language_array)
        viewHolder.languageRecyclerView2?.layoutManager =
          LinearLayoutManager(this.context,  RecyclerView.HORIZONTAL, false )
        viewHolder.languageRecyclerView2?.adapter = LanguageListAdapter2(studyDataSet[position][8] as MutableList<String>)
    }

    override fun getItemCount() = studyDataSet.size
}