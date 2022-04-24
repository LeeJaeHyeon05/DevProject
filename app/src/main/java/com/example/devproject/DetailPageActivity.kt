package com.example.devproject

import android.os.Bundle
import android.provider.ContactsContract
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_page)
        val position = intent.getIntExtra("position", 0)

        var conferTitleTextView : TextView = findViewById(R.id.conferTitleTextView)
        var conferImageView : ImageView = findViewById(R.id.conferImageView)
        var conferDateTextView : TextView = findViewById(R.id.conferDateTextView)
        var conferContentTextView : TextView = findViewById(R.id.conferConetentTextView)


        conferTitleTextView.text = DataHandler.textDataSet!![position][0]
        //DummyImage
        conferImageView.setImageResource(R.drawable.ic_launcher_foreground)
        conferDateTextView.text = DataHandler.textDataSet!![position][1]
        conferContentTextView.text = DataHandler.textDataSet!![position][2]

    }
}