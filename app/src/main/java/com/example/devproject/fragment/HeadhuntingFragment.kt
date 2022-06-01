package com.example.devproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.devproject.databinding.FragmentHeadhuntingBinding
import com.example.devproject.databinding.FragmentHomeBinding
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

        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}