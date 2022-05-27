package com.example.devproject.activity.study

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R
import com.example.devproject.databinding.ActivityShowConferenceDetailBinding
import com.example.devproject.dialog.DeleteDialog
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth

class ShowStudyDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowConferenceDetailBinding
    private var pos : Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowConferenceDetailBinding.inflate(layoutInflater)

        val position = intent.getIntExtra("position", 0)
        pos = position
        supportActionBar?.title = DataHandler.studyDataSet[pos!!][2] as String
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == DataHandler.studyDataSet[intent.getIntExtra("position", 0)][10].toString())) {
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
                    FirebaseIO.delete("groupstudyDocument", DataHandler.studyDataSet[intent.getIntExtra("position", 0)][9] as String )
                    Toast.makeText(this, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                    DataHandler.reload(DBType.STUDY)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}