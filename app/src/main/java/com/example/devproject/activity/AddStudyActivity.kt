package com.example.devproject.activity

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.databinding.ActivityAddStudyBinding
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.format.StudyInfo
import com.example.devproject.fragment.StudyFragment
import com.example.devproject.others.DBType
import com.example.devproject.others.LanguageListAdapter
import com.example.devproject.others.StudyListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class AddStudyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStudyBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var memberNumberPicker = binding.memberNumberPicker
        val data: Array<String> = Array(100){
                i -> (i+1).toString()
        }

        //language list view
        var typedArray : TypedArray = resources.obtainTypedArray(R.array.language_array)
        var languageSelectRecyclerView = binding.languageSelectRecyclerView
        languageSelectRecyclerView?.layoutManager = LinearLayoutManager(this.baseContext, LinearLayoutManager.HORIZONTAL, false)
        var adapter = LanguageListAdapter(typedArray)
        languageSelectRecyclerView?.adapter = adapter

        var totalMember : Long? = 0

        UIHandler.languageNumberTextView = binding.languageNumberTextView;

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

            val languageMap = LanguageListAdapter.getLanguageMaps()
            var languageArray : MutableList<String> = emptyList<String>().toMutableList()
            languageMap.forEach { if(it.value){
                    languageArray.add(it.key)
                }
            }


            val studyInfo = StudyInfo(
                documentID = documentID,
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

            if(FirebaseIO.write("groupstudyDocument", documentID, studyInfo)){
                DataHandler.reload(DBType.STUDY)
                Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}
