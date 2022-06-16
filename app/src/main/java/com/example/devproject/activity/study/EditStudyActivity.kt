package com.example.devproject.activity.study

import android.content.Intent
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
import com.example.devproject.util.DataHandler.Companion.studyDataSet
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.OneSignalUtil
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding
    private var position = intent.getIntExtra("position", 0)
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
                val dialog = BasicDialog(this, "편집할까요?")
                dialog.activate()
                dialog.okButton?.setOnClickListener {
                val studyInfo = StudyInfo(
                    documentID = studyDataSet[position].documentID.toString(),
                    ongoing = true,
                    title = binding.addStudyTitle.text.toString(),
                    content = binding.addStudyContent.text.toString(),
                    offline = !binding.studyOnlineCheckBox.isChecked,
                    studyURL = binding.addStudyLink.text.toString(),
                    totalMember = totalMember,
                    remainingMemeber = totalMember?.minus(binding.selectedMemberTextView.text.toString().toLong()),
                    language = adapter?.getLanguageList(),
                    uid = FirebaseAuth.getInstance().uid,
                    uploader= DataHandler.userInfo.id,
                    endDate = studyDataSet[position].endDate
                )

                if(FirebaseIO.write("groupstudyDocument", studyInfo.documentID.toString(), studyInfo)){
                    DataHandler.reload(DBType.STUDY)
                    Toast.makeText(this, "수정했어요", Toast.LENGTH_SHORT).show()
                }
                finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tableRow.visibility = View.VISIBLE
        binding.selectedMemberTextView.text = (Integer.parseInt(studyDataSet[position].totalMember.toString()) - Integer.parseInt(studyDataSet[position].remainingMemeber.toString())).toString()
        binding.studyMemberPlusButton.setOnClickListener {
            if( Integer.parseInt(binding.selectedMemberTextView.text as String) < Integer.parseInt(studyDataSet[position].totalMember.toString())) {
                binding.selectedMemberTextView.text = (Integer.parseInt(binding.selectedMemberTextView.text as String) + 1).toString()
            }

        }
        binding.studyMemberMinusButton.setOnClickListener {
            if( binding.selectedMemberTextView.text != "0"){
                binding.selectedMemberTextView.text = (Integer.parseInt(binding.selectedMemberTextView.text as String) - 1).toString()
            }
        }

        binding.addStudyTitle.setText(studyDataSet[position].title.toString())
        binding.addStudyContent.setText(studyDataSet[position].content.toString())
        binding.addStudyLink.setText(studyDataSet[position].studyURL.toString())
        binding.studyOnlineCheckBox.isChecked = !(studyDataSet[position].offline!!)
        var memberNumberPicker = binding.memberNumberPicker



        UIHandler.languageNumberTextView = binding.languageNumberTextView

        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageSelectRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        adapter = LanguageListAdapter(typedArray, studyDataSet[position].language)
        languageSelectRecyclerView?.adapter = adapter

        totalMember = studyDataSet[position].totalMember.toString().toLong()
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }
        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data
        memberNumberPicker.value = Integer.parseInt(studyDataSet[position].totalMember.toString())
        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            totalMember = picker.value.toLong()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ShowStudyDetailActivity::class.java)
        intent.putExtra("position", position)
        Toast.makeText(this, "편집 취소", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }
}