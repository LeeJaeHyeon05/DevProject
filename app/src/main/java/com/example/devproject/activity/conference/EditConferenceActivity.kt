package com.example.devproject.activity.conference

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R
import com.example.devproject.activity.MapActivity
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.dialog.PriceDialog
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.FirebaseIO.Companion.cloudWrite
import com.google.firebase.firestore.GeoPoint
import java.util.*

class EditConferenceActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding
    private var pos = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "컨퍼런스 편집"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var latitude: Double = 0.0
        var longitude: Double = 0.0
        val mGeocoder = Geocoder(this, Locale.getDefault())
        var list = mutableListOf<Address>()
        val position = intent.getIntExtra("position", 0)
        pos = position

        binding.addConTitle.setText(DataHandler.conferDataSet[position][1] as String)
        binding.startDateTextView.text = DataHandler.conferDataSet[position][10] as String
        binding.finishDateTextView.text = DataHandler.conferDataSet[position][11] as String
        binding.priceTextView.text = DataHandler.conferDataSet[position][3].toString()
        binding.addConLink.setText(DataHandler.conferDataSet[position][5] as String)
        binding.addConDetail.setText(DataHandler.conferDataSet[position][6] as String)
        binding.addConButton.text = "컨퍼런스 편집하기"

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
            val link = binding.addConLink.text.toString()

            val exceptWon = binding.priceTextView.text.split(" ")

            val price: Long = if(exceptWon[0] == "무료" || exceptWon[0] == ""){
                0
            } else Integer.parseInt(exceptWon[0]).toLong()

            val snapshotImage = findViewById<ImageView>(R.id.IvMapSnapshot)

            val conference = ConferenceInfo(
                conferenceURL = link,
                content = conContent,
                date = "",
                offline = !binding.conferOnlineCheckBox.isChecked,
                place = GeoPoint(latitude, longitude),
                price = price,
                title = conTitle,
                documentID = DataHandler.conferDataSet[position][8] as String,
                uploader = DataHandler.conferDataSet[position][0] as String,
                image = ArrayList<Uri>(),
                uid = DataHandler.conferDataSet[position][7] as String,
                startDate = binding.startDateTextView.text.toString().replace(",", "."),
                finishDate = binding.finishDateTextView.text.toString().replace(",", ".")
            )

            val bitmapDrawable: Drawable?
            val bitmap: Bitmap?

            if(latitude != 0.0 && longitude != 0.0){
                bitmapDrawable = snapshotImage.drawable
                bitmap = (bitmapDrawable as BitmapDrawable).bitmap

                if(checkInput(conference)){
                    FirebaseIO.delete("conferenceDocument", DataHandler.conferDataSet[position][8] as String)
                    if(cloudWrite(conference.documentID as String, bitmap, conference) && FirebaseIO.write("conferenceDocument", conference.documentID, conference)){
                        Toast.makeText(this, "편집 완료!", Toast.LENGTH_SHORT).show()

                        DataHandler.reload(DBType.CONFERENCE)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, ShowConferenceDetailActivity::class.java)
                            intent.putExtra("position", pos)
                            startActivity(intent)
                            finish()
                        }, 250)
                    }
                } else Toast.makeText(this, "빈칸을 모두 채워 주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                if(checkInput(conference)){
                    FirebaseIO.delete("conferenceDocument", DataHandler.conferDataSet[position][8] as String)
                    if(FirebaseIO.write("conferenceDocument", conference.documentID as String, conference)){
                        Toast.makeText(this, "편집 완료!", Toast.LENGTH_SHORT).show()

                        DataHandler.reload(DBType.CONFERENCE)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, ShowConferenceDetailActivity::class.java)
                            intent.putExtra("position", pos)
                            startActivity(intent)
                            finish()
                        }, 300)

                    }
                } else Toast.makeText(this, "빈칸을 모두 채워 주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkInput(conference: ConferenceInfo): Boolean{
        fun validateString(value: String?): Boolean? {
            return value?.isNotEmpty()
        }

        fun validateLong(value: Long?): Boolean{
            return value != null
        }

        return validateString(conference.documentID) == true &&
                validateString(conference.conferenceURL) == true &&
                validateString(conference.content) == true &&
                validateString(conference.startDate) == true &&
                validateString(conference.finishDate) == true &&
                validateString(conference.title) == true &&
                validateString(conference.uploader) == true && validateLong(conference.price)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDate() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        binding.startDateTextView.setOnClickListener{
            val dig = DatePickerDialog(this,
                { p0, year, month, day ->
                    binding.startDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
                }, year, month, day)
            dig.show()
        }

        binding.finishDateTextView.setOnClickListener{
            val dig = DatePickerDialog(this,
                { p0, year, month, day ->
                    binding.finishDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
                }, year, month, day)
            dig.show()
        }
    }

    private fun getPrice() {
        val priceBtn = binding.priceButton
        priceBtn.setOnClickListener {
            val dialog = PriceDialog(this)
            dialog.setOnOkClickedListener{ price->
                if(price == "무료"){
                    binding.priceTextView.text = price
                }
                else{
                    binding.priceTextView.text = "${price} 원"
                }
            }
            dialog.priceDia()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ShowConferenceDetailActivity::class.java)
        intent.putExtra("position", pos)
        startActivity(intent)
        finish()
    }
}