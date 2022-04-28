package com.example.devproject.format

import com.google.firebase.firestore.GeoPoint

data class ConferenceInfo(
    val documentID : String? = null,
    var conferenceURL: String? = null,
    var content: String? = null,
    var date: String? = null,
    var offline: Boolean? = null,
<<<<<<< HEAD
    var place: GeoPoint? = null,
=======
>>>>>>> 9a30b5324feb8a39e0ed4cbde1ec5561bffa1216
    var price: Int? = null,
    var title: String? = null,
    var uploader : String? = null
)