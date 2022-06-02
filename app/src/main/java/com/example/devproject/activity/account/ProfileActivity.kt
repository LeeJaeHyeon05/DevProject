package com.example.devproject.activity.account

import android.content.Intent
import android.content.res.TypedArray
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.example.devproject.activity.conference.EditConferenceActivity
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.databinding.ActivityProfileBinding
import com.example.devproject.dialog.FilterDialog
import com.example.devproject.format.UserInfo
import com.example.devproject.others.DBType
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.adapter.LanguageListAdapter2
import com.example.devproject.util.DataHandler
import com.example.devproject.util.DataHandler.Companion.conferenceNotiDeviceIDList
import com.example.devproject.util.DataHandler.Companion.studyNotiDeviceIDList
import com.example.devproject.util.DataHandler.Companion.userInfo
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1.FirestoreGrpc
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_profile.*

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

        supportActionBar?.title = DataHandler.userInfo.id + "의 프로필"

        binding.profileImageView.setImageResource(R.drawable.logo512)
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
        headhuntingRegisterSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){

            }else{

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