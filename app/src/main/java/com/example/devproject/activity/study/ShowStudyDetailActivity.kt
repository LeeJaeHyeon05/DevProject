package com.example.devproject.activity.study

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.databinding.ActivityShowStudyDetailBinding
import com.example.devproject.dialog.DeleteDialog
import com.example.devproject.others.DBType
import com.example.devproject.others.LanguageListAdapter2
import com.example.devproject.util.DataHandler
import com.example.devproject.util.DataHandler.Companion.studyDataSet
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth

class ShowStudyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowStudyDetailBinding
    private var pos : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowStudyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val position = intent.getIntExtra("position", 0)
        pos = position
        supportActionBar?.title = studyDataSet[pos!!][2] as String

        var studyRecyclerView = binding.languageRecyclerView
        studyRecyclerView.layoutManager =
            LinearLayoutManager(this.baseContext, RecyclerView.HORIZONTAL, false)
        studyRecyclerView.adapter = LanguageListAdapter2(studyDataSet[position][8] as MutableList<String>)

        binding.studyDetailContentTextView.text = studyDataSet[position][3].toString()
        binding.studyDetailOfflineTextView.text = if(studyDataSet[position][4] as Boolean){
            "오프라인"
        }else{
            "온라인"
        }
        binding.studyDetailLinkImageView.setOnClickListener {
            val intent = Intent(UIHandler.rootView?.context, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", studyDataSet[position][5].toString())
            UIHandler.rootView?.context?.startActivity(intent)
        }
        binding.studyDetailMemberTextView.text = "모집인원 ${studyDataSet[position][6]}명 중 ${studyDataSet[position][7]}명 모집 완료!"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == studyDataSet[intent.getIntExtra("position", 0)][10].toString())) {
            menuInflater.inflate(R.menu.actionbar_add_conference_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.editButton-> {
                if(FirebaseIO.isValidAccount()){
                    val intent = Intent(this, EditStudyActivity::class.java)
                    intent.putExtra("position", pos)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.deleteButton ->{
                val dialog = DeleteDialog(this)
                dialog.activate()
                dialog.okButton?.setOnClickListener {
                    FirebaseIO.delete("groupstudyDocument", studyDataSet[intent.getIntExtra("position", 0)][9] as String )
                    Toast.makeText(this, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                    DataHandler.reload(DBType.STUDY)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}