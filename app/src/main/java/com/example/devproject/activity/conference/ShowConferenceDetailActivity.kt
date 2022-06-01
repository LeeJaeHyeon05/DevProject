package com.example.devproject.activity.conference

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alespero.expandablecardview.ExpandableCardView
import com.bumptech.glide.Glide
import com.example.devproject.util.DataHandler
import com.example.devproject.R
import com.example.devproject.activity.ShowWebViewActivity
import com.example.devproject.dialog.BasicDialog
import com.example.devproject.others.ImageViewAdapter
import com.example.devproject.others.DBType
import com.example.devproject.util.DataHandler.Companion.conferDataSet
import com.example.devproject.util.FirebaseIO
import com.google.firebase.auth.FirebaseAuth

class ShowConferenceDetailActivity : AppCompatActivity() {

    var conferUploaderIconImageView : ImageView? = null
    var conferUploaderTextView : TextView? = null
    var conferTitleTextView : TextView? = null
    var conferRecyclerView : RecyclerView? = null
    var conferStartDateTextView : TextView? = null
    var conferFinishDateTextView : TextView? = null
    var conferPriceTextView : TextView? = null
    var conferOfflineTextView : TextView? = null
    var conferURLImageView : ImageView? = null
    var conferContentTextView : TextView? = null
    var confershowNoImage: ImageView? = null
    var conferManagerImageView : ImageView? = null

    private lateinit var imageAdapter: ImageViewAdapter
    lateinit var viewModel: ImageCounterViewModel

    var link : String? = null
    private var pos : Int? = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseIO.isValidAccount() && (FirebaseAuth.getInstance().uid == conferDataSet[intent.getIntExtra("position", 0)][7].toString())) {
            menuInflater.inflate(R.menu.actionbar_verified_menu, menu)
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
                    FirebaseIO.delete("conferenceDocument", conferDataSet[intent.getIntExtra("position", 0)][8] as String )
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
        setContentView(R.layout.activity_detail_page)
        val position = intent.getIntExtra("position", 0)
        pos = position

        supportActionBar?.title = conferDataSet[position][1].toString()

        conferUploaderIconImageView = findViewById(R.id.conferUploadeIconImageView)
        conferUploaderTextView  = findViewById(R.id.conferUploaderTextView)
        conferRecyclerView= findViewById(R.id.conferDetailRecyclerView)
        conferStartDateTextView = findViewById(R.id.conferStartDateTextView)
        conferFinishDateTextView = findViewById(R.id.conferFinishDateTextView)
        conferPriceTextView = findViewById(R.id.conferPriceTextView)
        conferOfflineTextView = findViewById(R.id.conferOfflineTextView)
        conferURLImageView = findViewById(R.id.conferURLImageView)
        conferContentTextView = findViewById(R.id.conferConetentTextView)
        confershowNoImage = findViewById(R.id.conferDetailImageView)
        conferManagerImageView = findViewById(R.id.conferManagerImageView)

        conferUploaderIconImageView?.setImageResource(R.drawable.logo512)
        conferUploaderTextView?.text = conferDataSet[position][0].toString()
        conferTitleTextView?.text = conferDataSet[position][1].toString()
        //DummyImage
        //conferImageView?.setImageResource(R.drawable.ic_launcher_foreground)
        conferStartDateTextView?.text = conferDataSet[position][10].toString().subSequence(5,12).toString() + " 부터"
        conferFinishDateTextView?.text = conferDataSet[position][11].toString().subSequence(5,12).toString() + " 까지"
        conferPriceTextView?.text = if(conferDataSet[position][3].toString().toInt() == 0) "무료" else "${conferDataSet[position][3]}원"
        conferOfflineTextView?.text = if(conferDataSet[position][4].toString() == "false") "온라인" else "오프라인"
        conferURLImageView?.setImageResource(R.drawable.link)
        conferManagerImageView?.visibility = if(conferDataSet[position][13] as Boolean){
            View.VISIBLE
        }else{
            View.INVISIBLE
        }
        //image = conferDataSet[position][9]
        showImage(position, this)

        viewModel = ViewModelProvider(this).get(ImageCounterViewModel::class.java)

        link = conferDataSet[position][5].toString()
        conferURLImageView?.setOnClickListener {
            val intent = Intent(this, ShowWebViewActivity::class.java)
            intent.putExtra("conferenceURL", link)
            this.startActivity(intent)
        }
        conferContentTextView?.text = conferDataSet[position][6].toString()
    }

    private fun showImage(position: Int, showConferenceDetailActivity: ShowConferenceDetailActivity) {
        val list: ArrayList<Uri> = conferDataSet[position][9] as ArrayList<Uri>
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
                if(list.size == 1 && conferDataSet[position][9].toString().contains("MapSnapShot.jpeg")){
                    confershowNoImage.visibility = View.VISIBLE
                    //imageList.add(imageUri.toUri())
                    //imageAdapter = ImageViewAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext)
                    //conferRecyclerView?.adapter = imageAdapter
                    //conferRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

                    val storageRef = FirebaseIO.storage.reference.child("${list[0]}")
                    storageRef.downloadUrl.addOnSuccessListener { image ->
                        var mapLayout = findViewById<ExpandableCardView>(R.id.detailPageMapSnapShot)
                        mapLayout.setTitle(conferDataSet[position][12].toString())
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
                    conferRecyclerView?.visibility = View.VISIBLE
                    confershowNoImage.visibility = View.INVISIBLE
                    val storageRef = FirebaseIO.storage.reference.child("${list[i]}")
                    storageRef.downloadUrl.addOnSuccessListener { image->
                        if(image.path!!.contains("MapSnapShot.jpeg")){
                            var mapLayout = findViewById<ExpandableCardView>(R.id.detailPageMapSnapShot)
                            mapLayout.setTitle(conferDataSet[position][12].toString())
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
                        imageAdapter = ImageViewAdapter(imageList = imageList, showConferenceDetailActivity.applicationContext, viewModel = viewModel)
                        conferRecyclerView?.adapter = imageAdapter
                        conferRecyclerView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                    }
                }
            }
        }
    }
}