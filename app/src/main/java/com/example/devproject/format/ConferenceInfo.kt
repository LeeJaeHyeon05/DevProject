package com.example.devproject.format

import com.google.firebase.firestore.GeoPoint

data class ConferenceInfo(
    var content: String? = null,
    var date: String? = null,
    var imageURL: String? = null,
    var offline: Boolean? = null,
    var place: GeoPoint? = null,
    var price: Int? = null,
    var title: String? = null,
    var uploader : String? = null
)