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
import com.example.devproject.util.DataHandler.Companion.conferDataSet
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth

class ShowConferenceDetailActivity : AppCompatActivity() {

    var conferUploaderIconImageView : ImageView? = null
    var conferUploaderTextView : TextView? = null
    var conferTitleTextView : TextView? = null
    var conferImageView : ImageView? = null
    var conferDateTextView : TextView? = null
    var conferPriceTextView : TextView? = null
    var conferOfflineTextView : TextView? = null
    var conferURLImageView : ImageView? = null
    var conferContentTextView : TextView? = null

    var link : String? = null
    private var pos : Int? = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == conferDataSet[intent.getIntExtra("position", 0)][7].toString())) {
            menuInflater.inflate(R.menu.actionbar_add_conference_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.editButton-> {
                if(FirebaseIO.isValidAccount()){
                    val intent = Intent(this, EditConferenceActivity::class.java)
                    intent.putExtra("position", pos)
                    startActivity(intent)
                }
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
        pos = position

        conferUploaderIconImageView = findViewById(R.id.conferUploadeIconImageView)
        conferUploaderTextView  = findViewById(R.id.conferUploaderTextView)
        conferTitleTextView = findViewById(R.id.conferTitleTextView)
        conferImageView= findViewById(R.id.conferImageView)
        conferDateTextView = findViewById(R.id.conferDateTextView)
        conferPriceTextView = findViewById(R.id.conferPriceTextView)
        conferOfflineTextView = findViewById(R.id.conferOfflineTextView)
        conferURLImageView = findViewById(R.id.conferURLImageView)
        conferContentTextView = findViewById(R.id.conferConetentTextView)

        conferUploaderIconImageView?.setImageResource(R.drawable.dev)
        conferUploaderTextView?.text = conferDataSet[position][0].toString()
        conferTitleTextView?.text = conferDataSet[position][1].toString()
        //DummyImage
        conferImageView?.setImageResource(R.drawable.ic_launcher_foreground)
        conferDateTextView?.text = conferDataSet[position][2].toString()
        conferPriceTextView?.text = if(conferDataSet[position][3].toString().toInt() == 0) "무료" else conferDataSet[position][3].toString()
        conferOfflineTextView?.text = if(conferDataSet[position][4].toString() == "false") "온라인" else "오프라인"
        conferURLImageView?.setImageResource(R.drawable.link)

        link = conferDataSet[position][5].toString()
        conferURLImageView?.setOnClickListener {
            val intent = Intent(this, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", link)
            this.startActivity(intent)
        }
        conferContentTextView?.text = conferDataSet[position][6].toString()
    }
}