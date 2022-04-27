package com.example.devproject.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.util.DataHandler
import com.example.devproject.R

class ShowConferenceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        val position = intent.getIntExtra("position", 0)

        var conferUploaderTextView : TextView = findViewById(R.id.conferUploaderTextView)
        var conferTitleTextView : TextView = findViewById(R.id.conferTitleTextView)
        var conferImageView : ImageView = findViewById(R.id.conferImageView)
        var conferDateTextView : TextView = findViewById(R.id.conferDateTextView)
        var conferPriceTextView : TextView = findViewById(R.id.conferPriceTextView)
        var conferOfflineTextView : TextView = findViewById(R.id.conferOfflineTextView)
        var conferURLImageView : ImageView = findViewById(R.id.conferURLImageView)
        var conferContentTextView : TextView = findViewById(R.id.conferConetentTextView)

        conferUploaderTextView.text = DataHandler.conferDataSet[position][0].toString()
        conferTitleTextView.text = DataHandler.conferDataSet[position][1].toString()
        //DummyImage
        conferImageView.setImageResource(R.drawable.ic_launcher_foreground)
        conferDateTextView.text = DataHandler.conferDataSet[position][2].toString()
        conferPriceTextView.text = DataHandler.conferDataSet[position][3].toString()
        conferOfflineTextView.text = DataHandler.conferDataSet[position][4].toString()
        conferURLImageView.setImageResource(R.drawable.link)
        conferContentTextView.text = DataHandler.conferDataSet[position][2].toString()
    }
}