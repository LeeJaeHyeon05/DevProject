package com.example.devproject.activity.study

import android.content.res.TypedArray
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Data
import com.example.devproject.R
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.StudyInfo
import com.example.devproject.others.DBType
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import com.onesignal.OneSignal
import org.json.JSONException
import org.json.JSONObject
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tableRow.visibility = View.INVISIBLE


        //language list view
        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageSelectRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray, null)
        languageSelectRecyclerView?.adapter = adapter

        var totalMember : Long? = 0

        UIHandler.languageNumberTextView = binding.languageNumberTextView
        var memberNumberPicker = binding.memberNumberPicker
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }
        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data
        totalMember = 1

        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            totalMember = picker.value.toLong()

        }
        var addStudyButton = binding.addStudyButton
        val documentID = "study${ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"))}"

        addStudyButton.setOnClickListener {

            val c = Calendar.getInstance()
            c.add(Calendar.DAY_OF_YEAR , 21)

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val studyInfo = StudyInfo(
                documentID = documentID,
                ongoing = true,
                title = binding.addStudyTitle.text.toString(),
                content = binding.addStudyContent.text.toString(),
                offline = !binding.studyOnlineCheckBox.isChecked,
                studyURL = binding.addStudyLink.text.toString(),
                totalMember = totalMember,
                remainingMemeber = totalMember,
                language = adapter.getLanguageList(),
                uid = FirebaseAuth.getInstance().uid,
                uploader= DataHandler.userInfo.id,
                endDate = "${year}. ${month+1}. $day",
            )

            if(FirebaseIO.write("groupstudyDocument", documentID, studyInfo)){

                var deviceIDs = ""
                DataHandler.studyNotiDeviceIDList.forEachIndexed { index, s ->
                    deviceIDs += if(index + 1 == DataHandler.studyNotiDeviceIDList.size ){
                        "'${s}'"
                    }else{
                        "'${s}', "
                    }
                }

                //Notification
                try {
                    OneSignal.postNotification("{'headings' : {'en' : '신규 스터디'}, 'contents': {'en':'${studyInfo.title}'}, 'include_player_ids': [${deviceIDs}]}",
                        object : OneSignal.PostNotificationResponseHandler {
                            override fun onSuccess(response: JSONObject) {
                                Log.i("OneSignalExample", "postNotification Success: $response")
                            }

                            override fun onFailure(response: JSONObject) {
                                Log.e("OneSignalExample", "postNotification Failure: $response")
                            }
                        })
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                DataHandler.reload(DBType.STUDY)
                Toast.makeText(this, "업로드했어요!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
