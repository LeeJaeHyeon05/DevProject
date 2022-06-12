package com.example.devproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.adapter.HeadhuntingListAdapter
import com.example.devproject.databinding.FragmentHeadhuntingBinding
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler
import com.example.devproject.util.UIHandler

class HeadhuntingFragment : Fragment() {
    private var mBinding : FragmentHeadhuntingBinding? = null

    companion object{
        var adapter : HeadhuntingListAdapter? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHeadhuntingBinding.inflate(inflater, container, false)
        mBinding = binding

        UIHandler.mainActivity?.supportActionBar?.title = "헤드헌팅"
        var headhuntingRecyclerView = mBinding?.headhuntingRecyclerView
        headhuntingRecyclerView?.layoutManager =  GridLayoutManager(this.context, 3, RecyclerView.VERTICAL, false)
        adapter =HeadhuntingListAdapter()
        headhuntingRecyclerView?.adapter = adapter

        val swipeRefreshLayout= mBinding?.swipeRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener {
            DataHandler.reload(DBType.HEADHUNTING)
            swipeRefreshLayout.isRefreshing = false
        }

        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}