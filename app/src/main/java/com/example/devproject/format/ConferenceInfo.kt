package com.example.devproject.format

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class ConferenceInfo(
    val documentID : String = "",
    var conferenceURL: String? = null,
    var content: String? = null,
    var date: String? = "",
    var offline: Boolean = true,
    var place: String = "",
    var price: Long? = null,
    var title: String = "",
    var uploader : String? = null,
    val image: MutableList<Uri>? = null,
    var uid : String? = null,
    var startDate : String = "",
    var finishDate : String  = "",
    var manager : Boolean  = false,
    var tag : String = "",
)