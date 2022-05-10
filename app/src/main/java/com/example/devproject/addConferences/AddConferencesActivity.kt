package com.example.devproject.addConferences

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.devproject.R
import com.example.devproject.activity.MapActivity
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.dialog.PriceDialog
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.FirebaseIO.Companion.storageWrite
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_login.*
import org.w3c.dom.Text
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddConferencesActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding
    private lateinit var uploader: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "컨퍼런스 추가"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var latitude: Double = 0.0
        var longitude: Double = 0.0
        val mGeocoder = Geocoder(this, Locale.getDefault())
        var list = mutableListOf<Address>()
        uploader = ""

        getDate()

        getPrice()

        //칩
        tagClip()

        //업로더 아이디 가져오기
        findUploader()

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

            val tag = binding.conferChipGroup.toString()

            //월 불러오기
            val id = "document" + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"))
            var uid = FirebaseAuth.getInstance().uid
            val docNumText = id
            val date = binding.dateTextView.text.toString().replace(",", ".")

            val snapshotImage = findViewById<ImageView>(R.id.IvMapSnapshot)

            val conference = ConferenceInfo(
                conferenceURL = link,
                content = conContent,
                date = date,
                offline = checkOffline(snapshotImage),
                place = GeoPoint(latitude, longitude),
                price = price,
                title = conTitle,
                documentID = id,
                uploader = uploader,
                uid = uid

            )

            val bitmapDrawable: Drawable?
            val bitmap: Bitmap?

            if(latitude != 0.0 && longitude != 0.0){
                bitmapDrawable = snapshotImage.drawable
                bitmap = (bitmapDrawable as BitmapDrawable).bitmap

                if(checkInput(conference)){
                    if(storageWrite(docNumText, bitmap) && FirebaseIO.write("conferenceDocument", docNumText, conference)){
                        Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
                        DataHandler.reload()
                        finish()
                    }
                } else Toast.makeText(this, "빈칸을 모두 채워 주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                if(checkInput(conference)){
                    if(FirebaseIO.write("conferenceDocument", docNumText, conference)){
                        Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
                        DataHandler.reload()
                        finish()
                    }
                } else Toast.makeText(this, "빈칸을 모두 채워 주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun tagClip() {
        val chipEdiText = binding.addClip
        val chipAddButton = binding.addChipButton
        val chipGroup = binding.conferChipGroup
        val chipCountText = binding.tagNumberTextView

        //만약 editText 없다면
        chipAddButton.setOnClickListener {
            val editString = chipEdiText.text.toString()
            if (editString.isEmpty()) {
                Toast.makeText(this, "내용을 추가해주세요", Toast.LENGTH_SHORT).show()
                chipAddButton.isEnabled
            } else {
                chipGroup.addView(Chip(this).apply {
                    text = editString
                    isCloseIconVisible = true
                    setChipIconResource(R.drawable.ic_baseline_code_24)
                    setOnCloseIconClickListener {
                        chipGroup.removeView(this)
                    }
                })
            }
        }
        chipCountText.text
        val chipList = ArrayList<String>()
        for (i: Int in 1..chipGroup.childCount) {
            val chip: Chip = chipGroup.getChildAt(i - 1) as Chip
            chipList.add(chip.text.toString())
        }

        var output = "count: ${chipList.size}\n"
        for (i in chipList) {
            output += "$i / "
        }
        showToast(output)

    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
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
                validateString(conference.date) == true &&
                validateString(conference.title) == true &&
                validateString(conference.uploader) == true && validateLong(conference.price)
    }

    private fun checkOffline(image: ImageView) = image.drawable == null

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

    private fun findUploader(){
        val getEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

        val id = getEmail.split("@")

        uploader = id[0]
//        var id: String? = null
//        val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//
//        mFirestore.collection("UserInfo")
//            .whereEqualTo("uid", getEmail)
//            .get()
//            .addOnCompleteListener {
//                if(it.isSuccessful){
//                    for(document in it.result.documents){
//                        handleUploader(document.id)
//                    }
//                }
//            }
//        return id
    }
//
//    private fun handleUploader(query: String){
//        uploader = query
//    }

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
                    binding.dateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
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
}