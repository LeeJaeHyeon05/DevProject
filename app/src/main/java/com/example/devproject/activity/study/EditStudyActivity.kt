package com.example.devproject.activity.study

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.StudyInfo
import com.example.devproject.others.DBType
import com.example.devproject.adapter.LanguageListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.DataHandler.Companion.studyDataSet
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth

class EditStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding
    private var pos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var position = intent.getIntExtra("position", 0)
        pos = position

        val endDate = studyDataSet[pos][11].toString()
        binding.tableRow.visibility = View.VISIBLE
        binding.selectedMemberTextView.text = (Integer.parseInt(studyDataSet[pos][6].toString()) - Integer.parseInt(studyDataSet[pos][7].toString())).toString()
        binding.studyMemberPlusButton.setOnClickListener {
            if( Integer.parseInt(binding.selectedMemberTextView.text as String) < Integer.parseInt(studyDataSet[pos][6].toString())) {
                binding.selectedMemberTextView.text = (Integer.parseInt(binding.selectedMemberTextView.text as String) + 1).toString()
            }

        }
        binding.studyMemberMinusButton.setOnClickListener {
            if( binding.selectedMemberTextView.text != "0"){
                binding.selectedMemberTextView.text = (Integer.parseInt(binding.selectedMemberTextView.text as String) - 1).toString()
            }
        }

        binding.addStudyTitle.setText(studyDataSet[pos][2].toString())
        binding.addStudyContent.setText(studyDataSet[pos][3].toString())
        binding.addStudyLink.setText(studyDataSet[pos][5].toString())
        binding.studyOnlineCheckBox.isChecked = !(studyDataSet[pos][4] as Boolean)
        var memberNumberPicker = binding.memberNumberPicker



        UIHandler.languageNumberTextView = binding.languageNumberTextView

        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageSelectRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray, studyDataSet[pos][8] as MutableList<String>)
        languageSelectRecyclerView?.adapter = adapter

        var totalMember : Long = studyDataSet[pos][6].toString().toLong()
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }
        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data
        memberNumberPicker.value = Integer.parseInt(studyDataSet[pos][6].toString())
        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            totalMember = picker.value.toLong()

        }

        binding.addStudyButton.text = "스터디 모집글 편집하기"
        binding.addStudyButton.setOnClickListener {

            val studyInfo = StudyInfo(
                documentID = studyDataSet[pos!!][9].toString(),
                ongoing = true,
                title = binding.addStudyTitle.text.toString(),
                content = binding.addStudyContent.text.toString(),
                offline = !binding.studyOnlineCheckBox.isChecked,
                studyURL = binding.addStudyLink.text.toString(),
                totalMember = totalMember,
                remainingMemeber = totalMember - binding.selectedMemberTextView.text.toString().toLong(),
                language = adapter.getLanguageList(),
                uid = FirebaseAuth.getInstance().uid,
                uploader= DataHandler.userInfo.id,
                endDate = endDate
            )

            if(FirebaseIO.write("groupstudyDocument", studyInfo.documentID.toString(), studyInfo)){
                DataHandler.reload(DBType.STUDY)
                Toast.makeText(this, "수정했어요", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ShowStudyDetailActivity::class.java)
        intent.putExtra("position", pos)
        Toast.makeText(this, "편집 취소", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }
}