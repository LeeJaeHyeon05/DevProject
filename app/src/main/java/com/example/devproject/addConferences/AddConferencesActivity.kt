package com.example.devproject.addConferences

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.devproject.R
import com.example.devproject.activity.MainActivity
import com.example.devproject.activity.MapActivity
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.others.ListAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
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
        setContentView(binding.root)

        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var mGeocoder = Geocoder(this, Locale.getDefault())
        var list = mutableListOf<Address>()

        getDate()

        getPrice()

        var startMapActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> //지도 액티비티 결과값 받아오기
            if (result?.resultCode ?: 0 == Activity.RESULT_OK) {
                latitude  = result?.data?.getDoubleExtra("latitude", 0.0)?: 0.0
                longitude = result?.data?.getDoubleExtra("longitude", 0.0)?: 0.0

                var snapShot: ByteArray = result?.data?.getByteArrayExtra("snapshot")!!


                if(snapShot != null){
                    binding.showMapSnapShotLayout.setOnExpandedListener { view, isExpanded ->
                        view.findViewById<ImageView>(R.id.IvMapSnapshot).setImageBitmap(
                            BitmapFactory.decodeByteArray(snapShot, 0, snapShot.size))
                    }
                    binding.showMapSnapShotLayout.expand()
                }
                list = mGeocoder.getFromLocation(latitude, longitude, 1)

                var address = list[0].getAddressLine(0)

                binding.ETConferenceGeo.text = Editable.Factory.getInstance().newEditable(address.toString())
            }
        }

        binding.TextLayoutConferenceGeo.setEndIconOnClickListener { //지도액티비티 부르기
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("currentLat", latitude)
            intent.putExtra("currentLng", longitude)
            startMapActivityResult.launch(intent)
        }

        binding.addConButton.setOnClickListener {
            //editText 불러오기
            val conTitle = binding.addConTitle.text.toString()
            val conContent = binding.addConDetail.text.toString()
            val place = binding.ETConferenceGeo.text.toString()

            val exceptWon = binding.priceTextView.text.split(" ")
            val price = Integer.parseInt(exceptWon[0])

            //월 불러오기
            val todayMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            //합치기 document + 날짜 + 숫자 플러스
            val docNumText = "document$todayMonth"
            val date = binding.dateTextView.text.toString()

            //위치 불러오기


            //구조 설정

            //val id = findUploader()

            val conference = ConferenceInfo(
                conferenceURL = "www.naver.com",
                content = conContent,
                date = date,
                offline = false,
                place = GeoPoint(latitude, longitude),
                price = price,
                title = conTitle,
                documentID = "1",
                uploader = FirebaseAuth.getInstance().currentUser?.email.toString()
            )
            if(FirebaseIO.write("conferenceDocument", docNumText, conference)){
                Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
//                DataHandler.load()
                finish()
            }
        }
    }

    private fun findUploader(): String?{
        var getUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        var id: String? = null
        val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

        mFirestore.collection("UserInfo")
            .whereEqualTo("uid", getUid)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!.documents){
                        id = document.toString()
                    }
                }
            }
        return id
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDate() {
        val dateBtn = binding.datePickButton

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        dateBtn.setOnClickListener {
            val dig = DatePickerDialog(this,
                { p0, year, month, day ->
                    binding.dateTextView.text = "$year, ${month+1}, $day"
                }, year, month, day)
            dig.show()
        }
    }

    private fun getPrice() {
        val priceBtn = binding.priceButton
        priceBtn.setOnClickListener {
            val dialog = PriceDialog(this)
            dialog.setOnOkClickedListener{ price->
                binding.priceTextView.text = price + " 원"
            }
            dialog.priceDia()
        }
    }
}