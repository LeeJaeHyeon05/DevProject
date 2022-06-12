package com.example.devproject.activity.conference

import android.content.Intent
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alespero.expandablecardview.ExpandableCardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.devproject.R
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.adapter.ImageSliderAdapter
import com.example.devproject.databinding.ActivityShowConferenceDetailBinding
import com.example.devproject.dialog.BasicDialog
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler
import com.example.devproject.util.DataHandler.Companion.conferDataSet
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator


class ShowConferenceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowConferenceDetailBinding

    var conferPager: ViewPager2? = null
    var numPage = 3

    private lateinit var brightIndicator: DotsIndicator
    private lateinit var darkerIndicator: DotsIndicator
    private lateinit var imageAdapter: ImageSliderAdapter
    lateinit var viewModel: ImageCounterViewModel

    var link : String? = null
    private var pos : Int? = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == conferDataSet[intent.getIntExtra("position", 0)].uid)) {
            menuInflater.inflate(R.menu.actionbar_document_edit_menu, menu)
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
                    finish()
                }
            }
            R.id.deleteButton ->{
                val dialog = BasicDialog(this, "정말 삭제할까요?")
                dialog.activate()
                dialog.okButton?.setOnClickListener {
                    FirebaseIO.delete("conferenceDocument", conferDataSet[intent.getIntExtra("position", 0)].documentID)
                    Toast.makeText(this, "삭제했어요", Toast.LENGTH_SHORT).show()
                    DataHandler.reload(DBType.CONFERENCE)
                    finish()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowConferenceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val position = intent.getIntExtra("position", 0)
        pos = position

        supportActionBar?.title = conferDataSet[position].title

        var conferUploaderIconImageView = binding.conferUploadeIconImageView
        var conferUploaderTextView  = binding.conferUploaderTextView
        //conferRecyclerView= findViewById(R.id.conferDetailRecyclerView)
        var conferStartDateTextView = binding.conferStartDateTextView
        var conferFinishDateTextView = binding.conferFinishDateTextView
        var conferPriceTextView = binding.conferPriceTextView
        var conferOfflineTextView = binding.conferOfflineTextView
        var conferURLImageView = binding.conferURLImageView
        var conferContentTextView = binding.conferConetentTextView
        //confershowNoImage = findViewById(R.id.conferDetailImageView)
        var conferManagerImageView = binding.conferManagerImageView

        var typedArray : TypedArray = resources.obtainTypedArray(R.array.position_array)
        FirebaseIO.db.collection("UserInfo").document(conferDataSet[position].uploader!!).get().addOnSuccessListener {
           conferUploaderIconImageView?.setImageDrawable(typedArray.getDrawable(it["position"].toString().toInt()))
        }

        conferUploaderTextView?.text = conferDataSet[position].uploader
        conferStartDateTextView?.text = conferDataSet[position].startDate.subSequence(2,12).toString()
        conferFinishDateTextView?.text = conferDataSet[position].finishDate.subSequence(2,12).toString()
        conferPriceTextView?.text = if(conferDataSet[position].price!!.toInt() == 0) "무료" else "${conferDataSet[position].price}원"
        conferOfflineTextView?.text = if(!conferDataSet[position].offline) "온라인" else "오프라인"
        conferURLImageView?.setImageResource(R.drawable.link)
        conferPager = findViewById(R.id.detailViewPager)
        brightIndicator = findViewById(R.id.circleIndicator)
        darkerIndicator = findViewById(R.id.circleIndicator2)
        conferManagerImageView?.visibility = if(conferDataSet[position].manager as Boolean){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }
        //image = conferDataSet[position][9]
        showImage(position, this)

        viewModel = ViewModelProvider(this).get(ImageCounterViewModel::class.java)

        link = conferDataSet[position].conferenceURL
        conferURLImageView?.setOnClickListener {
            val intent = Intent(this, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", link)
            this.startActivity(intent)
        }
        conferContentTextView?.text = conferDataSet[position].content


    }

    private fun showImage(position: Int, showConferenceDetailActivity: ShowConferenceDetailActivity) {
        val list: ArrayList<Uri> = conferDataSet[position].image as ArrayList<Uri>
        val imageList: ArrayList<Uri> = ArrayList()
        val confershowNoImage = findViewById<ImageView>(R.id.conferDetailImageView)
        if(list.isEmpty()){
            //val imageUri = "android.resource://${packageName}/"+R.drawable.dev
            confershowNoImage.visibility = View.VISIBLE
            //imageList.add(imageUri.toUri())
            //imageAdapter = ImageViewAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext)
            //conferRecyclerView?.adapter = imageAdapter
            //conferRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            return
        }
        else{
            for(i in list.indices){
                if(list.size == 1 && conferDataSet[position].image.toString().contains("MapSnapShot.jpeg")){
                    confershowNoImage.visibility = View.VISIBLE
                    conferPager?.visibility = View.INVISIBLE
                    //imageList.add(imageUri.toUri())
                    //imageAdapter = ImageViewAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext)
                    //conferRecyclerView?.adapter = imageAdapter
                    //conferRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

                    val storageRef = FirebaseIO.storage.reference.child("${list[0]}")
                    storageRef.downloadUrl.addOnSuccessListener { image ->
                        var mapLayout = findViewById<ExpandableCardView>(R.id.detailPageMapSnapShot)
                        mapLayout.setTitle(conferDataSet[position].place)
                        mapLayout.setOnExpandedListener { view, isExpanded ->
                            Glide.with(view)
                                .load(image)
                                .fitCenter()
                                .into(view.findViewById(R.id.IvMapSnapshot))
                        }
                        mapLayout.visibility = View.VISIBLE
                        mapLayout.expand()
                    }
                }
                else {
                    conferPager?.visibility = View.VISIBLE
                    confershowNoImage.visibility = View.INVISIBLE
                    val storageRef = FirebaseIO.storage.reference.child("${list[i]}")
                    storageRef.downloadUrl.addOnSuccessListener { image->
                        if(image.path!!.contains("MapSnapShot.jpeg")){
                            var mapLayout = findViewById<ExpandableCardView>(R.id.detailPageMapSnapShot)
                            mapLayout.setTitle(conferDataSet[position].place)
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
                            imageList.add(image)
                        }
                    }.addOnSuccessListener {
                        imageList.sort()

                        var temp: FloatArray? = floatArrayOf()

                        Glide.with(this)
                            .asBitmap()
                            .load(imageList[0])
                            .into(object: CustomTarget<Bitmap?>(){
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?
                                ) {
                                    temp = calculateBrightnessEstimate(resource, 1)
                                    val resizedBackgroundBrightness = temp?.get(0)
                                    var colorInt = getColorStringFromBrightness(resizedBackgroundBrightness!!.toInt())

                                    if(imageList.size > 1){
                                        when(colorInt){
                                            1 -> { //어두운 이미지일때
                                                imageAdapter = ImageSliderAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext, numPage)
                                                conferPager?.adapter = imageAdapter
                                                conferPager?.let { it1 -> brightIndicator.setViewPager2(it1) }
                                                conferPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                                                conferPager?.currentItem = 0
                                                conferPager?.offscreenPageLimit = 3
                                                brightIndicator.visibility = View.VISIBLE
                                            }
                                            2 -> { //밝은 이미지일때
                                                imageAdapter = ImageSliderAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext, numPage)
                                                conferPager?.adapter = imageAdapter
                                                conferPager?.let { it1 -> darkerIndicator.setViewPager2(it1) }
                                                conferPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                                                conferPager?.currentItem = 0
                                                conferPager?.offscreenPageLimit = 3
                                                darkerIndicator.visibility = View.VISIBLE
                                            }
                                        }
                                    }
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                    return
                                }

                            })

                        imageAdapter = ImageSliderAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext, numPage)
                        conferPager?.adapter = imageAdapter
//                        conferPager?.let { it1 -> indicator.setViewPager2(it1) }
//                        conferPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
//                        conferPager?.currentItem = 0
//                        conferPager?.offscreenPageLimit = 3
                        //conferPager?.adapter?.registerAdapterDataObserver(indicator.adapterDataObserver)
                    }
                }
            }
        }
    }

    private fun calculateBrightnessEstimate(bitmap: Bitmap, pixelSpacing: Int): FloatArray? {
        var R = 0
        var G = 0
        var B = 0
        var T = 0
        var S = 0f
        val height = bitmap.height
        val width = bitmap.width
        var n = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 0
        while (i < pixels.size) {
            val color = pixels[i]
            val hsv = FloatArray(3)
            R += Color.red(color)
            G += Color.green(color)
            B += Color.blue(color)
            T += getColorToBrightness(color)
            Color.colorToHSV(color, hsv)
            S += hsv[1]
            n++
            i += pixelSpacing * 15
        }

        //T = (R + B + G);
        return floatArrayOf((T / n).toFloat(), S / n)
    }


    //컬러의 값을 밝기로값으로 가져오기
    private fun getColorToBrightness(color: Int): Int {
        val R = Color.red(color)
        val G = Color.green(color)
        val B = Color.blue(color)
        return Math.sqrt(R * R * .241 + G * G * .691 + B * B * .068).toInt()
    }


    //bitmap의 밝기값을 매개값에 넣는다. 리턴값으로 bitmap이미지위의 텍스트 색상 출력
    private fun getColorStringFromBrightness(brightness: Int): Int {
        return if (brightness < 195) {
            1
        } else {
            2
        }
    }
}