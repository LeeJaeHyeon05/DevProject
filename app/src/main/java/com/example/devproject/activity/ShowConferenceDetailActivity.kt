package com.example.devproject.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.R
import com.example.devproject.dialog.DeleteDialog
import com.example.devproject.dialog.PriceDialog
import com.example.devproject.util.DataHandler.Companion.conferDataSet
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth
import com.google.firestore.v1.FirestoreGrpc
import kotlinx.android.synthetic.main.dialog_find_password.*

class ShowConferenceDetailActivity : AppCompatActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == conferDataSet[intent.getIntExtra("position", 0)][7].toString())) {
            menuInflater.inflate(R.menu.actionbar_add_conference_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.editButton-> {
            }
            R.id.deleteButton ->{
                val dialog = DeleteDialog(this)
                dialog.deleteDialog()
                dialog.okButton?.setOnClickListener {
                    FirebaseIO.delete("conferenceDocument", conferDataSet[intent.getIntExtra("position", 0)][8] as String)
                    Toast.makeText(this, "삭제 되었습니다", Toast.LENGTH_SHORT).show()
                    DataHandler.reload()
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        val position = intent.getIntExtra("position", 0)

        var conferUploaderIconImageView : ImageView = findViewById(R.id.conferUploadeIconImageView)
        var conferUploaderTextView : TextView = findViewById(R.id.conferUploaderTextView)
        var conferTitleTextView : TextView = findViewById(R.id.conferTitleTextView)
        var conferImageView : ImageView = findViewById(R.id.conferImageView)
        var conferDateTextView : TextView = findViewById(R.id.conferDateTextView)
        var conferPriceTextView : TextView = findViewById(R.id.conferPriceTextView)
        var conferOfflineTextView : TextView = findViewById(R.id.conferOfflineTextView)
        var conferURLImageView : ImageView = findViewById(R.id.conferURLImageView)
        var conferContentTextView : TextView = findViewById(R.id.conferConetentTextView)

        conferUploaderIconImageView.setImageResource(R.drawable.dev)
        conferUploaderTextView.text = conferDataSet[position][0].toString()
        conferTitleTextView.text = conferDataSet[position][1].toString()
        //DummyImage
        conferImageView.setImageResource(R.drawable.ic_launcher_foreground)
        conferDateTextView.text = conferDataSet[position][2].toString()
        conferPriceTextView.text = if(conferDataSet[position][3].toString().toInt() == 0) "무료" else conferDataSet[position][3].toString()
        conferOfflineTextView.text = if(conferDataSet[position][4].toString() == "false") "온라인" else "오프라인"
        conferURLImageView.setImageResource(R.drawable.link)
        conferURLImageView.setOnClickListener {
            val intent = Intent(this, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", conferDataSet[position][5].toString())
            this.startActivity(intent)
        }
        conferContentTextView.text = conferDataSet[position][6].toString()
    }
}