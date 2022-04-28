package com.example.devproject.format

data class ConferenceInfo(
    val documentID : String? = null,
    var conferenceURL: String? = null,
    var content: String? = null,
    var date: String? = null,
    var offline: Boolean? = null,
    var price: Int? = null,
    var title: String? = null,
    var uploader : String? = null
)