package com.example.devproject.fragment

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.activity.account.LoginActivity
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.databinding.FragmentSignUpNecessaryBinding
import com.example.devproject.databinding.FragmentSignUpOptionalBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpOptionalFragment : Fragment() {

    private var password : String?= null
    private lateinit var  auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            password = bundle.getString("bundleKey").toString()
        }

        val binding = FragmentSignUpOptionalBinding.inflate(inflater, container, false)

        //language list view
        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray, null)
        languageSelectRecyclerView?.adapter = adapter

        binding.signUpPrevButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainerView, UIHandler.signUpNecessaryFragment)
                ?.commit()
        }

        binding.sigunUpButton.setOnClickListener {
            DataHandler.signUpUserInfo.language = adapter.getLanguageList()
            DataHandler.signUpUserInfo.gitLink = binding.gitLinkEditText.text.toString()
            auth.createUserWithEmailAndPassword(DataHandler.signUpUserInfo.Email.toString(), password!!).addOnCompleteListener {
                if(it.isSuccessful){
                    FirebaseIO.write("UserInfo", DataHandler.signUpUserInfo.id.toString(), DataHandler.signUpUserInfo)

                    Toast.makeText(this.context, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    val mIntent = Intent(activity, LoginActivity::class.java)
                    mIntent.putExtra("LoginId", DataHandler.signUpUserInfo.Email)
                    mIntent.putExtra("LoginPassword", password)
                    activity?.setResult(AppCompatActivity.RESULT_OK, mIntent)
                    activity?.finish()
                }else{
                    Toast.makeText(this.context, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        return binding?.root
    }
}