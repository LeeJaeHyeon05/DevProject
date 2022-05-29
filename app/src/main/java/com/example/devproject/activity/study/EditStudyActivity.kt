package com.example.devproject.activity.study

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devproject.R
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.StudyInfo
import com.example.devproject.others.DBType
import com.example.devproject.others.LanguageListAdapter
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


        binding.addStudyTitle.setText(studyDataSet[pos][2].toString())
        binding.addStudyContent.setText(studyDataSet[pos][3].toString())
        binding.addStudyLink.setText(studyDataSet[pos][5].toString())
        binding.studyOnlineCheckBox.isChecked = !(studyDataSet[pos][4] as Boolean)
        var memberNumberPicker = binding.memberNumberPicker
        memberNumberPicker.value = Integer.parseInt(studyDataSet[pos][6].toString())

        UIHandler.languageNumberTextView = binding.languageNumberTextView

        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageSelectRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray, studyDataSet[pos][8] as MutableList<String>)
        languageSelectRecyclerView?.adapter = adapter

        var totalMember : Long = 0
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }
        memberNumberPicker.minValue = 1
        memberNumberPicker.maxValue = data.size-1
        memberNumberPicker.wrapSelectorWheel = false
        memberNumberPicker.displayedValues = data

        memberNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            totalMember = picker.value.toLong()

        }

        binding.addStudyButton.text = "스터디 모집글 편집하기"
        binding.addStudyButton.setOnClickListener {

            val languageMap = adapter.getLanguageMaps()
            var languageArray : MutableList<String> = emptyList<String>().toMutableList()
            languageMap.forEach { if(it.value){
                languageArray.add(it.key)
            }
            }
            println(languageArray)

            val studyInfo = StudyInfo(
                documentID = studyDataSet[pos!!][9].toString(),
                ongoing = true,
                title = binding.addStudyTitle.text.toString(),
                content = binding.addStudyContent.text.toString(),
                offline = !binding.studyOnlineCheckBox.isChecked,
                studyURL = binding.addStudyLink.text.toString(),
                totalMember = totalMember,
                remainingMemeber = totalMember,
                language = languageArray,
                uid = FirebaseAuth.getInstance().uid,
                uploader= DataHandler.userInfo.id
            )

            if(FirebaseIO.write("groupstudyDocument", studyInfo.documentID.toString(), studyInfo)){
                DataHandler.reload(DBType.STUDY)
                Toast.makeText(this, "수정하였습니다", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ShowStudyDetailActivity::class.java)
        intent.putExtra("position", pos)
        startActivity(intent)
        finish()
    }
}