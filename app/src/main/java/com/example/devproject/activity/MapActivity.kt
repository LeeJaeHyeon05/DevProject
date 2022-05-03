package com.example.devproject.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.devproject.R
import com.example.devproject.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var binding: ActivityMapBinding

    private var mMap: GoogleMap? = null
    var currentLat: Double = 0.0
    var currentLng: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentLat = intent.getDoubleExtra("currentLat", 37.500045)
        currentLng = intent.getDoubleExtra("currentLng", 127.036506)

        var mGeocoder = Geocoder(this, Locale.getDefault())

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        var list = mutableListOf<Address>()

        binding.BtnAddressSearch.setOnClickListener {
            if(binding.EtAddress != null){
                var softKeyboard: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                softKeyboard.hideSoftInputFromWindow(binding.EtAddress.windowToken, 0)
                list = mGeocoder.getFromLocationName(binding.EtAddress.text.toString(), 10)

                if(list.size > 0){
                    currentLat = list[0].latitude
                    currentLng = list[0].longitude

                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLat, currentLng), 16f))

                    setMarker()

                    list = mGeocoder.getFromLocation(currentLat, currentLng, 1)
                }
                else{
                    Snackbar.make(window.decorView.rootView, "좀더 자세한 주소를 입력하세요" , Snackbar.LENGTH_LONG).show()
                }
            }
        }

        binding.BtnMapOk.setOnClickListener {
            var intent = Intent()

            var snapshot: Bitmap? = null
            var stream = ByteArrayOutputStream()
            var byte = byteArrayOf()


            mMap?.let { googleMap ->

                googleMap.snapshot {
                    if (it != null) {
                        it.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                        byte = stream.toByteArray()

                        stream.flush()
                        it.recycle()
                    }

                    intent.putExtra("snapshot", byte)
                    intent.putExtra("latitude", googleMap.cameraPosition.target.latitude)
                    intent.putExtra("longitude", googleMap.cameraPosition.target.longitude)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }

        binding.BtnMapCancel.setOnClickListener { finish() }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap?.let{
            //val currentLocation = LatLng(currentLat, currentLng)
            val currentLocation = LatLng(37.500045, 127.036506)
            it.setMaxZoomPreference(20.0f)
            it.setMinZoomPreference(12.0f)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16f))
        } //지도가 처음 켜졌을때 나타나는 지도의 화면 초기화하는곳

        setMarker()
    }

    private fun setMarker(){
        mMap?.let{
            it.clear()

            val markerOptions = MarkerOptions()
            markerOptions.position(it.cameraPosition.target)
            markerOptions.title("마커 위치")

            val marker = it.addMarker(markerOptions)

            it.setOnCameraMoveListener {
                marker?.let{marker ->
                    marker.position = it.cameraPosition.target
                }
            } //지도가 움직일때 마커도 같이 움직이게 하는 것
        }
    }


}