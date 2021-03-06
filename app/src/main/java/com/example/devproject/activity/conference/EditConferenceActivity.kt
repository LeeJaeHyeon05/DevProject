package com.example.devproject.activity.conference

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alespero.expandablecardview.ExpandableCardView
import com.bumptech.glide.Glide
import com.example.devproject.R
import com.example.devproject.activity.MapActivity
import com.example.devproject.databinding.ActivityAddConferencesBinding
import com.example.devproject.dialog.PriceDialog
import com.example.devproject.format.ConferenceInfo
import com.example.devproject.others.DBType
import com.example.devproject.others.ImageViewAdapter
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class EditConferenceActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAddConferencesBinding
    private var pos = 0
    lateinit var viewModel: ImageCounterViewModel
    private lateinit var imageAdapter: ImageViewAdapter
    private var originalImageList: ArrayList<Uri> = ArrayList()
    private var editImageList: ArrayList<Uri> = ArrayList()
    private var deleteImageList = ArrayList<Uri>()
    private var checkOffline = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddConferencesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.title = "???????????? ??????"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        var latitude: Double = 0.0
        var longitude: Double = 0.0
        val mGeocoder = Geocoder(this, Locale.getDefault())
        var list = mutableListOf<Address>()
        var position = intent.getIntExtra("position", 0)
        pos = position
        val imageRecyclerView = binding.addConferenceImageRecyclerView

        binding.addConTitle.setText(DataHandler.conferDataSet[position][1] as String)
        binding.startDateTextView.text = DataHandler.conferDataSet[position][10] as String
        binding.finishDateTextView.text = DataHandler.conferDataSet[position][11] as String
        binding.priceTextView.text = DataHandler.conferDataSet[position][3].toString()
        binding.addConLink.setText(DataHandler.conferDataSet[position][5] as String)
        binding.addConDetail.setText(DataHandler.conferDataSet[position][6] as String)
        binding.addConButton.text = "???????????? ????????????"
        val imagelist: ArrayList<Uri> = DataHandler.conferDataSet[position][9] as ArrayList<Uri>
        var snapshotImage = findViewById<ImageView>(R.id.IvMapSnapshot)
        
        getDate()
        getPrice()
        showImage(position, this)

        viewModel = ViewModelProvider(this).get(ImageCounterViewModel::class.java)

        viewModel.imageCounterValue.observe(this, androidx.lifecycle.Observer {
            binding.addConImageTextView.text = "$it / 3"
        })


        var startMapActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result -> //?????? ???????????? ????????? ????????????
            if (result?.resultCode ?: 0 == Activity.RESULT_OK) {
                latitude  = result?.data?.getDoubleExtra("latitude", 0.0)?: 0.0
                longitude = result?.data?.getDoubleExtra("longitude", 0.0)?: 0.0

                var snapShot: ByteArray = result?.data?.getByteArrayExtra("snapshot")!!


                if(snapShot != null){
                    binding.showMapSnapShotLayout.collapse()
                    binding.showMapSnapShotLayout.setOnExpandedListener { view, isExpanded ->
                        view.findViewById<ImageView>(R.id.IvMapSnapshot).setImageBitmap(
                            BitmapFactory.decodeByteArray(snapShot, 0, snapShot.size))
                    }
                    binding.showMapSnapShotLayout.expand()
                }
                list = mGeocoder.getFromLocation(latitude, longitude, 1)

                var address = list[0].getAddressLine(0)

                binding.ETConferenceGeo.text = Editable.Factory.getInstance().newEditable(address.toString())
                binding.showMapSnapShotLayout.setTitle("?????? ?????????")
            }
        }

        binding.TextLayoutConferenceGeo.setEndIconOnClickListener { //?????????????????? ?????????
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("currentLat", latitude)
            intent.putExtra("currentLng", longitude)
            startMapActivityResult.launch(intent)
        }

        val startGetImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result-> //?????? ????????????
            if(result.data != null){
                val imageData = result.data
                var size: Int = imageData?.clipData?.itemCount!!
                if(originalImageList.isNotEmpty()){ //???????????? ????????? ???????????? ???????????? ?????? ???????????????
                    for(i in originalImageList){
                        if(editImageList.contains(i)){
                            continue
                        }
                        else{
                            editImageList.add(i)
                        }
                    }
                }
                imageAdapter = ImageViewAdapter(imageList = editImageList, this, deleteImageList = imagelist, viewModel)
                val imageSize = UIHandler.countImage(result, editImageList, this, imageRecyclerView, imageAdapter, viewModel).toString()
                viewModel.updateValue(imageSize.toInt())


//                if(size != null){
//                    val imageUri = imageData?.clipData
//                    if(editImageList.size < 4){
//                        if (imageUri != null) {
//                            if(editImageList.size + size < 4){
//                                for(i in 0 until size){
//                                    editImageList.add(result.data!!.clipData!!.getItemAt(i).uri)
//                                }
//                            }
//                            else{
//                                var index = 0
//                                while(editImageList.size != 3){
//                                    editImageList.add(result.data!!.clipData!!.getItemAt(index).uri)
//                                    index++
//                                }
//                                Toast.makeText(this, "???????????? 3????????? ?????????????????????", Toast.LENGTH_SHORT).show()
//                            }
//                            editImageList.sortDescending()
//                            imageAdapter = ImageViewAdapter(imageList = editImageList, this, deleteImageList = imagelist)
//                            imageRecyclerView.adapter = imageAdapter
//                            imageRecyclerView.layoutManager =  LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//                        }
//                    }
//                }
            }
        }

        binding.addConImageButtonLayout.setOnClickListener { //?????? ????????????
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startGetImageResult.launch(intent)
        }

        binding.conferOnlineCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.TextLayoutConferenceGeo.visibility = View.GONE
                binding.showMapSnapShotLayout.visibility = View.GONE
                checkOffline = false

            }else{ //?????????????????? ????????? ??????????????? ?????????????????? ??????
                binding.TextLayoutConferenceGeo.visibility = View.VISIBLE
                binding.showMapSnapShotLayout.visibility = View.VISIBLE
                checkOffline = true
            }
        }

        binding.addConButton.setOnClickListener {
            //editText ????????????
            val conTitle = binding.addConTitle.text.toString()
            val conContent = binding.addConDetail.text.toString()
            val link = binding.addConLink.text.toString()

            val exceptWon = binding.priceTextView.text.split(" ")

            val price: Long = if(exceptWon[0] == "??????" || exceptWon[0] == ""){
                0
            } else Integer.parseInt(exceptWon[0]).toLong()

            if(imageAdapter.itemCount != -1){
                this.deleteImageList = imageAdapter.getDeleteImage()
            }

            var place = DataHandler.conferDataSet[position][12] as String

            val conference = initConference(link, conContent, place, snapshotImage ,price, conTitle, position)

            if(checkInput(conference)){
                editConference(conference, position, snapshotImage)
            } else Toast.makeText(this, "????????? ?????? ?????? ?????????", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initConference(
        link: String,
        conContent: String,
        place: String,
        snapshotImage: ImageView,
        price: Long,
        conTitle: String,
        position: Int
    ): ConferenceInfo {
        var switchPlace = ""
        if(!binding.conferOnlineCheckBox.isChecked){
            if (binding.ETConferenceGeo.text.isNotEmpty()) {
                switchPlace = binding.ETConferenceGeo.text.toString()
            } else {
                switchPlace = place
            }
        }else{
            switchPlace = ""
        }

        return ConferenceInfo(
            conferenceURL = link,
            content = conContent,
            date = "",
            offline = checkOffline,
            place = switchPlace,
            price = price,
            title = conTitle,
            documentID = DataHandler.conferDataSet[position][8] as String,
            uploader = DataHandler.conferDataSet[position][0] as String,
            image = DataHandler.conferDataSet[position][9] as ArrayList<Uri>,
            uid = DataHandler.conferDataSet[position][7] as String,
            startDate = binding.startDateTextView.text.toString().replace(",", "."),
            finishDate = binding.finishDateTextView.text.toString().replace(",", ".")
        )
    }

    private fun editConference(conference: ConferenceInfo, position: Int, snapshotImage: ImageView){
        if(editImageList.isEmpty()){
            editImageList = originalImageList
        }

        //FirebaseIO.delete("conferenceDocument", "document${DataHandler.conferDataSet[position][8] as String}")
        if(deleteImageList.isNotEmpty()){
            for(i in deleteImageList.indices){
                Log.d("TAG", "onCreate: ${deleteImageList[i]}")
                val storageRef = FirebaseIO.storage.reference.child("${deleteImageList[i]}")
                storageRef.delete().addOnCompleteListener {
                    Log.d("TAG", "delete: ${it.isComplete}")
                }
            }
        }
        if(FirebaseIO.storageWrite(
                "conferenceDocument",
                DataHandler.conferDataSet[position][8] as String,
                snapshotImage,
                editImageList,
                conference
            )
        ) {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
            CoroutineScope(Dispatchers.Main).launch {
                DataHandler.reload(DBType.CONFERENCE)

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(
                        applicationContext,
                        ShowConferenceDetailActivity::class.java
                    )
                    intent.putExtra("position", pos)
                    startActivity(intent)
                    finish()
                }, 1000)

            }
            finish()
        }
    }

    private fun showImage(position: Int, editConferenceActivity: EditConferenceActivity) {
        val list: ArrayList<Uri> = DataHandler.conferDataSet[position][9] as ArrayList<Uri>
        val confershowNoImage = findViewById<ImageView>(R.id.conferDetailImageView)
        if(list.isEmpty()){
            imageAdapter = ImageViewAdapter(imageList = originalImageList, editConferenceActivity.applicationContext, deleteImageList = DataHandler.conferDataSet[position][9] as ArrayList<Uri>, viewModel = viewModel)
            binding.addConferenceImageRecyclerView.adapter = imageAdapter
            binding.addConferenceImageRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            return
        }
        else{
            for(i in list.indices){
                val storageRef = FirebaseIO.storage.reference.child("${list[i]}")
                storageRef.downloadUrl.addOnSuccessListener { image->
                    if(image.path!!.contains("MapSnapShot.jpeg")){
                        var mapLayout = findViewById<ExpandableCardView>(R.id.showMapSnapShotLayout)
                        mapLayout.setTitle(DataHandler.conferDataSet[position][12].toString())
                        mapLayout.setOnExpandedListener { view, isExpanded ->
                            Glide.with(view)
                                .load(image)
                                .fitCenter()
                                .into(view.findViewById(R.id.IvMapSnapshot))
                        }
                        mapLayout.visibility = View.VISIBLE
                        mapLayout.expand()
                    }
                    else{
                        originalImageList.add(image)
                    }
                }.addOnSuccessListener {
                    viewModel.updateValue(originalImageList.size)
                    originalImageList.sort()
                    imageAdapter = ImageViewAdapter(imageList = originalImageList, editConferenceActivity.applicationContext, deleteImageList = DataHandler.conferDataSet[position][9] as ArrayList<Uri>, viewModel = viewModel)
                    binding.addConferenceImageRecyclerView.adapter = imageAdapter
                    binding.addConferenceImageRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                }
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
                if(price == "??????"){
                    binding.priceTextView.text = price
                }
                else{
                    binding.priceTextView.text = "${price} ???"
                }
            }
            dialog.priceDia()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ShowConferenceDetailActivity::class.java)
        intent.putExtra("position", pos)
        Toast.makeText(this, "?????? ??????", Toast.LENGTH_SHORT).show()
        startActivity(intent)
        finish()
    }
}