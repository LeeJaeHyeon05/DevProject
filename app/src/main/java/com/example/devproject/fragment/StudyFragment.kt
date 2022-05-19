package com.example.devproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.databinding.FragmentStudyBinding
import com.example.devproject.others.DBType
import com.example.devproject.others.ListAdapter
import com.example.devproject.others.StudyListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler

class StudyFragment : Fragment() {
    private var mBinding : FragmentStudyBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStudyBinding.inflate(inflater, container, false)
        mBinding = binding

        val swipeRefreshLayout= mBinding?.swiperStudyRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener {
            DataHandler.reload(DBType.STUDY)
            swipeRefreshLayout.isRefreshing = false
        }

        var studyRecyclerView = mBinding?.studyRecyclerView
        studyRecyclerView?.layoutManager = GridLayoutManager(this.context,2)
        studyRecyclerView?.adapter = StudyListAdapter()


        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}