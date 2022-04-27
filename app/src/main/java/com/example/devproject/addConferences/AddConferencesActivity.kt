package com.example.devproject.addConferences

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddConferencesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConferencesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val conButton = binding.addConButton

        val db = Firebase.firestore

        conButton.setOnClickListener {
            //editText 불러오기
            val conTitle = binding.addConTitle.text.toString()
            val conContent = binding.addConDetail.text.toString()

            //월 불러오기
            val todayMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            //합치기 document + 날짜 + 숫자 플러스
            val docNumText = "document$todayMonth"
            val date = getDate().toString()

            //구조 설정
            val deverence = Dev(conTitle, conContent, date)

            //db에 보내기
            db.collection("conferenceDocument").document(docNumText)
                .set(deverence)
                .addOnSuccessListener {
                    finish()
                    Toast.makeText(this, "업로드 성공!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    finish()
                    Toast.makeText(this, "업로드 실패 다시 시도해 주세요", Toast.LENGTH_SHORT).show()
                }

        }
        getDate()

        getPrice()
    }
    private fun getDate() {
        val dateBtn = binding.datePickButton

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        dateBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, _, _ ->
                binding.dateTextView.text = "날짜 : $year, $month, $day"
            }, year, month, day)
            dpd.show()
        }

    }

    private fun getPrice() {
        val priceBtn = binding.priceButton
        priceBtn.setOnClickListener {
            val dialog = PriceDialog(this)
            dialog.priceDia()
        }
    }

}