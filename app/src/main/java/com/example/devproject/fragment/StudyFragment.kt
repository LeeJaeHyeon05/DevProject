package com.example.devproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.activity.AddStudyActivity
import com.example.devproject.databinding.FragmentStudyBinding
import com.example.devproject.others.DBType
import com.example.devproject.others.StudyListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO

class StudyFragment : Fragment() {
    private var mBinding : FragmentStudyBinding? = null

    companion object{
        var adapter : StudyListAdapter? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataHandler.load(DBType.STUDY)

        val binding = FragmentStudyBinding.inflate(inflater, container, false)
        mBinding = binding

        val swipeRefreshLayout= mBinding?.swiperStudyRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener {
            DataHandler.reload(DBType.STUDY)
            swipeRefreshLayout.isRefreshing = false
        }

        var studyRecyclerView = mBinding?.studyRecyclerView
        studyRecyclerView?.layoutManager = GridLayoutManager(this.context, 2, RecyclerView.VERTICAL, false)
        adapter = StudyListAdapter()
        studyRecyclerView?.adapter = adapter

        val addStudyButton = mBinding?.floatingActionButton
        addStudyButton?.setOnClickListener {
            if(FirebaseIO.isValidAccount()){
                val intent = Intent(activity, AddStudyActivity::class.java)
                startActivity(intent)
            }else {
                Toast.makeText(this.context, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
            }
        }

        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}