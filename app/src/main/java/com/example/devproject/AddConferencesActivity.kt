package com.example.devproject

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import com.example.devproject.MainActivity.Companion.TAG
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AddConferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConferencesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val conTitle = binding.addConTitle
        val conContent = binding.addConDetail
        val conButton = binding.addConButton

        val db = Firebase.firestore

        conButton.setOnClickListener {
            val deverence = hashMapOf(
                "title" to conTitle,
                "content" to conContent

            )
            db.collection("conference")
                .add(deverence)
                .addOnSuccessListener {
                    Log.d(TAG, "성공")
                }
                .addOnFailureListener {
                    Log.d(TAG, "실패")
                }
        }


        val dateBtn = binding.datePickButton
        val Date = Calendar.getInstance()
        val year = Date.get(Calendar.YEAR)
        val month = Date.get(Calendar.MONTH)
        val day = Date.get(Calendar.DAY_OF_MONTH)

        dateBtn.setOnClickListener {
            val dateDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, _, dayOfMonth ->

            }, year, month, day)
            dateDialog.show()
        }

    }
}