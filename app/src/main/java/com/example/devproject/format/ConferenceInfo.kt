package com.example.devproject.format

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class ConferenceInfo(
    val documentID : String? = null,
    var conferenceURL: String? = null,
    var content: String? = null,
    var date: String? = "",
    var offline: Boolean? = null,
    var place: String? = "",
    var price: Long? = null,
    var title: String? = null,
    var uploader : String? = null,
    val image: MutableList<Uri>? = null,
    var uid : String? = null,
    var startDate : String? = null,
    var finishDate : String?  = null,
    var manager : Boolean?  = false,
)