package com.example.devproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.study.ShowStudyDetailActivity
import com.example.devproject.activity.ShowWebViewActivity
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
        val studyCardView : CardView = view.findViewById(R.id.studyCardView)
        val studyPreDateTextView : TextView = view.findViewById(R.id.studyPreDateTextView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.study_list_item, viewGroup, false)
        context = view.context
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //Binding Image & Text data Set trough firebase
        if (studyDataSet.size == 0) return
        viewHolder.studyPreOngoingTextView.text =
            if (studyDataSet[position][0] as Boolean) "모집중" else "마감"
        viewHolder.studyPreTitleTextView.text = studyDataSet[position][2].toString()

        if(!(studyDataSet[position][0] as Boolean)){
            viewHolder.studyPreOngoingTextView.setBackgroundColor(Color.rgb(234, 84, 84))
            viewHolder.studyPreDateTextView.setBackgroundColor(Color.rgb(234, 84, 84))
        }

        viewHolder.studyOfflineTextView.text =
            if (!(studyDataSet[position][4] as Boolean)) "온라인" else "오프라인"
        viewHolder.studyLinkImageView.setImageResource(R.drawable.link)
        viewHolder.studyLinkImageView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", studyDataSet[position][5].toString())
            UIHandler.rootView?.context?.startActivity(intent)
        }
        viewHolder.remainingMemeberTextView.text = studyDataSet[position][7].toString() + "명 남음!"

        viewHolder.languageRecyclerView2?.layoutManager =
            LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
        viewHolder.languageRecyclerView2?.adapter =
            LanguageListAdapter2(studyDataSet[position][8] as MutableList<String>)
        viewHolder.studyCardView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowStudyDetailActivity::class.java)
            intent.putExtra("position", position)
            UIHandler.rootView?.context?.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        var endDate = studyDataSet[position][11].toString()
        viewHolder.studyPreDateTextView.text = endDate.substring(2, endDate.length) + "까지"
    }

    override fun getItemCount() = studyDataSet.size
}