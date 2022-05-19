package com.example.devproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.activity.conference.AddConferencesActivity
import com.example.devproject.databinding.FragmentHomeBinding
import com.example.devproject.others.DBType
import com.example.devproject.others.ListAdapter
import com.example.devproject.others.StudyListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler

class HomeFragment : Fragment() {
    private var mBinding : FragmentHomeBinding? = null

    companion object{
        var adapter : ListAdapter? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding = binding

        var conferRecyclerView = mBinding?.conferRecyclerView
        conferRecyclerView?.layoutManager = LinearLayoutManager(this.context)
        adapter = ListAdapter()
        conferRecyclerView?.adapter = adapter

        val swipeRefreshLayout= mBinding?.swiperConferRefreshLayout
        swipeRefreshLayout?.setOnRefreshListener {
            DataHandler.reload(DBType.CONFERENCE)
            swipeRefreshLayout.isRefreshing = false
        }


        val addCon = mBinding?.conferAddButton
        addCon?.setOnClickListener {
            if(FirebaseIO.isValidAccount()){
                val intent = Intent(activity, AddConferencesActivity::class.java)
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