package com.example.devproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.adapter.HeadhuntingListAdapter
import com.example.devproject.adapter.ListAdapter
import com.example.devproject.databinding.FragmentHeadhuntingBinding
import com.example.devproject.util.UIHandler

class HeadhuntingFragment : Fragment() {
    private var mBinding : FragmentHeadhuntingBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHeadhuntingBinding.inflate(inflater, container, false)
        mBinding = binding

        UIHandler.mainActivity?.supportActionBar?.title = "헤드헌팅"
        var headhuntingRecyclerView = mBinding?.headhuntingRecyclerView
        headhuntingRecyclerView?.layoutManager = LinearLayoutManager(this.context)
        headhuntingRecyclerView?.adapter = HeadhuntingListAdapter()

        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}