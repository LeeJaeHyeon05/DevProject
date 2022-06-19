package com.example.devproject.activity.account

import android.content.res.TypedArray
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.databinding.FragmentSignUpNecessaryBinding
import com.example.devproject.databinding.FragmentSignUpOptionalBinding
import com.example.devproject.util.UIHandler

class SignUpOptionalFragment : Fragment() {

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.actionbar_signup_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.nextButton -> {
//
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignUpOptionalBinding.inflate(inflater, container, false)

        //language list view
        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray, null)
        languageSelectRecyclerView?.adapter = adapter

        return binding?.root
    }
}