package com.example.devproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.devproject.databinding.ActivityInsertConferenceBinding
import java.util.*

class InsertConferenceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertConferenceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInsertConferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var mGeocoder = Geocoder(this, Locale.getDefault())
        var list = mutableListOf<Address>()

        var startMapActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result?.resultCode ?: 0 == Activity.RESULT_OK) {
                latitude  = result?.data?.getDoubleExtra("latitude", 0.0)?: 0.0
                longitude = result?.data?.getDoubleExtra("longitude", 0.0)?: 0.0

                var snapShot: ByteArray = result?.data?.getByteArrayExtra("snapshot")!!
                binding.IvMapSnapshot.setImageBitmap(BitmapFactory.decodeByteArray(snapShot, 0, snapShot.size))

                list = mGeocoder.getFromLocation(latitude, longitude, 1)

                var address = list[0].getAddressLine(0)

                binding.ETConferenceGeo.text = Editable.Factory.getInstance().newEditable(address.toString())
            }
        }

        binding.insertConferenceExpandableLayout.setOnExpandedListener { viewExpand, isExpanded ->
            viewExpand.findViewById<CalendarView>(R.id.insertConferenceCalendar).setOnDateChangeListener { view, year, month, day ->
                binding.insertConferenceExpandableLayout.setTitle("${year}년 ${month + 1}월 ${day}일")
                Log.d("TAG", "onCreate: ${year}년 ${month + 1}월 ${day}일")
            }
        }

        binding.TextLayoutConferenceGeo.setEndIconOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("currentLat", latitude)
            intent.putExtra("currentLng", longitude)
            startMapActivityResult.launch(intent)
        }
    }
}