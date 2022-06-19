package com.example.devproject.activity.study

import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.StudyInfo
import com.example.devproject.others.DBType
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.dialog.BasicDialog
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.OneSignalUtil
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding
    var totalMember : Long? = 0
    var adapter : LanguageListAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actionbar_register_menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {

            android.R.id.home -> {
                finish()
                return true
            }

            R.id.registerButton -> {
                val dialog = BasicDialog(this, "정말 등록할까요?")
                dialog.activate()
                dialog.okButton?.setOnClickListener {

                    val documentID = "study${ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"))}"

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
                        language = adapter?.getLanguageList(),
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
                        OneSignalUtil.post("신규 스터디", studyInfo.title, deviceIDs)
                        DataHandler.reload(DBType.STUDY)
                        Toast.makeText(this, "등록했어요!", Toast.LENGTH_SHORT).show()
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
        adapter = LanguageListAdapter(typedArray, null)
        languageSelectRecyclerView?.adapter = adapter

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
    }
}
