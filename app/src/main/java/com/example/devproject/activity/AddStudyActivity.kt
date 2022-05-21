package com.example.devproject.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.format.StudyInfo
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding
    private lateinit var uploader: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        findUploader()
        var memberNumberPicker = binding.memberNumberPicker
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }

        var totalMember : Long? = 0

        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data
        totalMember = 1

        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            totalMember =  picker.value.toLong()

        }

        var addStudyButton = binding.addStudyButton
        val documentID = "study${ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"))}"

        addStudyButton.setOnClickListener {
            val studyInfo = StudyInfo(
                documentID = documentID,
                ongoing = true,
                title = binding.addStudyTitle.text.toString(),
                content = binding.addStudyContent.text.toString(),
                offline = !binding.studyOnlineCheckBox.isChecked,
                studyURL = binding.addStudyLink.text.toString(),
                totalMember = totalMember,
                remainingMemeber = totalMember,
                uid = FirebaseAuth.getInstance().uid,
                uploader=uploader
            )

            if(FirebaseIO.write("groupstudyDocument", documentID, studyInfo)){
                Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun findUploader(){
        val getEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        CoroutineScope(Dispatchers.Main).launch {
            mFirestore.collection("UserInfo")
                .whereEqualTo("email", getEmail)
                .get()
                .addOnSuccessListener {
                    for(document in it){
                        val string = document["id"] as String
                        uploader = string
                    }
                }
                .addOnFailureListener{
                    Log.d("TAG", "findUploader: ${it.stackTrace}")
                }
        }
    }

}
