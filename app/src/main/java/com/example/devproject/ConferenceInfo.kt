package com.example.devproject

data class ConferenceInfo(
    var content: String? = null,
    var date: String? = null,
    var imageURL: String? = null,
    var offline: Boolean? = null,
    var place: String? = null,
    var price: Int? = null,
    var title: String? = null,
    var uploader : String? = null
)