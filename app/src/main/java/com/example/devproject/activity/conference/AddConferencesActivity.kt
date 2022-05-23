package com.example.devproject.activity.conference

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.activity.MapActivity
import com.example.devproject.others.ImageViewAdapter
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.dialog.PriceDialog
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO.Companion.storageWrite
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddConferencesActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding
    private lateinit var uploader: String
    private lateinit var imageAdapter: ImageViewAdapter

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
        val imageList = ArrayList<Uri>()
        val imageRecyclerView = binding.addConferenceImageRecyclerView

        uploader = ""

        setDatePrice()

        val startGetImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result-> //사진 불러오기
            if(result.data != null){
                val imageData = result.data
                val size = imageData?.clipData?.itemCount
                val imageUri = imageData?.clipData
                if (imageUri != null) {
                    for(i in 0 until size!!){
                        imageList.add(result.data!!.clipData!!.getItemAt(i).uri)
                    }
                    imageList.sort()
                    imageAdapter = ImageViewAdapter(imageList = imageList, this)
                    imageRecyclerView.adapter = imageAdapter
                    imageRecyclerView.layoutManager =  LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                }
            }
        }

        binding.addConImageBtn.setOnClickListener { //사진 불러오기
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startGetImageResult.launch(intent)
        }

        getDate()
        getPrice()
        //칩
        tagClip()

        val startMapActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> //지도 액티비티 결과값 받아오기
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

        binding.conferOnlineCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.TextLayoutConferenceGeo.visibility = View.GONE
                binding.showMapSnapShotLayout.visibility = View.GONE
            }else{
                binding.TextLayoutConferenceGeo.visibility = View.VISIBLE
                binding.showMapSnapShotLayout.visibility = View.VISIBLE
            }
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
            var offline = !binding.conferOnlineCheckBox.isChecked

            //월 불러오기
            val documentId = "document" + ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS")).toString()
            val uid = FirebaseAuth.getInstance().uid

            val snapshotImage = findViewById<ImageView>(R.id.IvMapSnapshot)

            val conference = ConferenceInfo(
                conferenceURL = link,
                content = conContent,
                date = binding.startDateTextView.text.toString().replace(",", "."),
                offline = offline,
                place = GeoPoint(latitude, longitude),
                price = price,
                title = conTitle,
                documentID = documentId,
                uploader = DataHandler.userInfo.id,
                image = imageList,
                uid = uid,
                startDate =  binding.startDateTextView.text.toString().replace(",", "."),
                finishDate =  binding.finishDateTextView.text.toString().replace(",", ".")
            )

            if(checkInput(conference)){
                if(storageWrite("conferenceDocument", documentId, snapshotImage, imageList, conference)){
                    Toast.makeText(this, "업로드했습니다", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.Main).launch {
                        DataHandler.reload(DBType.CONFERENCE)
                    }
                    finish()
                }
            }else Toast.makeText(this, "빈칸을 모두 채워 주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDatePrice(){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.startDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
        binding.priceTextView.text = "무료"
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
        //이 밑에 파트를 갯수 파트로 변경 예정
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
                validateString(conference.startDate) == true &&
                validateString(conference.finishDate) == true &&
                validateString(conference.title) == true &&
                validateString(conference.uploader) == true && validateLong(conference.price) && validateString(conference.image.toString()) == true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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

        binding.startDateTextView.setOnClickListener {
            val dig = DatePickerDialog(this,
                { p0, year, month, day ->
                    binding.startDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
                    binding.finishDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
                }, year, month, day)
            dig.show()
        }

        binding.finishDateTextView.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this,
                { p0, year, month, day ->
                    binding.finishDateTextView.text = "$year. ${if(month + 1 < 10) "0" + (month + 1) else  (month + 1)}. ${if(day < 10) "0" + day else day}"
                }, year, month, day)
           datePickerDialog.show()
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