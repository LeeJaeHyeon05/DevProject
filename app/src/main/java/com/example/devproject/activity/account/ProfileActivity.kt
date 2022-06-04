package com.example.devproject.activity.account

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.example.devproject.databinding.ActivityProfileBinding
import com.example.devproject.format.UserInfo
import com.example.devproject.adapter.LanguageListAdapter2
import com.example.devproject.util.DataHandler
import com.example.devproject.util.DataHandler.Companion.conferenceNotiDeviceIDList
import com.example.devproject.util.DataHandler.Companion.headhuntingUserList
import com.example.devproject.util.DataHandler.Companion.studyNotiDeviceIDList
import com.example.devproject.util.DataHandler.Companion.userInfo
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_profile_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.editButton -> {
                if(FirebaseIO.isValidAccount()){
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                }
            }

            R.id.logoutButton -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = userInfo.id + "의 프로필"
        UIHandler.profileImageView = binding.profileImageView

        var typedArray : TypedArray = resources.obtainTypedArray(R.array.position_array)
        binding.profileImageView.setImageDrawable(typedArray.getDrawable(userInfo.position!!.toInt()))


        var userId =  OneSignal.getDeviceState()?.userId
        val conferenceNotiSwitch = binding.conferenceNotiSwitch
        if(conferenceNotiDeviceIDList.contains(userId)){
            conferenceNotiSwitch.isChecked = true
        }

        //language list view
        var languageSelectRecyclerView = binding.languageRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        languageSelectRecyclerView?.adapter = LanguageListAdapter2(userInfo.languages!!)

        conferenceNotiSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                if (userId != null) {
                    conferenceNotiDeviceIDList.add(userId)
                }
                FirebaseIO.db.collection("onesignalInfo").document("conferenceNotification").update("deviceID", conferenceNotiDeviceIDList)
            }else{
                conferenceNotiDeviceIDList.remove(userId)
                FirebaseIO.db.collection("onesignalInfo").document("conferenceNotification").update("deviceID", conferenceNotiDeviceIDList)
            }
        }

        val headhuntingRegisterSwitch = binding.headhuntingRegisterSwitch
        headhuntingRegisterSwitch.isChecked = headhuntingUserList.contains(userInfo.id)
        headhuntingRegisterSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                headhuntingUserList.add(userInfo.id!!)
                FirebaseIO.db.collection("etc").document("headhunting").update("users", headhuntingUserList)
            }else{
                headhuntingUserList.remove(userInfo.id!!)
                FirebaseIO.db.collection("etc").document("headhunting").update("users", headhuntingUserList)
            }
        }

        val studyNotiSwitch = binding.studyNotiSwitch
        if(studyNotiDeviceIDList.contains(userId)){
            studyNotiSwitch.isChecked = true
        }

        studyNotiSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                if (userId != null) {
                    studyNotiDeviceIDList.add(userId)
                }
                FirebaseIO.db.collection("onesignalInfo").document("studyNotification").update("deviceID", studyNotiDeviceIDList)
            }else{
                studyNotiDeviceIDList.remove(userId)
                FirebaseIO.db.collection("onesignalInfo").document("studyNotification").update("deviceID", studyNotiDeviceIDList)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun logout(){
        FirebaseAuth.getInstance().signOut()
        DataHandler.userInfo = UserInfo()
        Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}