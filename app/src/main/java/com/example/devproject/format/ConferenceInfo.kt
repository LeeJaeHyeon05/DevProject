package com.example.devproject.format

import android.net.Uri
import com.google.firebase.firestore.GeoPoint

data class ConferenceInfo(
    val documentID : String? = null,
    var conferenceURL: String? = null,
    var content: String? = null,
    var date: String? = null,
    var offline: Boolean? = null,
    var place: GeoPoint? = null,
    var price: Long? = null,
    var title: String? = null,
    var uploader : String? = null,
    val image: MutableList<Uri>? = null
){
    fun toArray() : Array<Any> {
        val array = arrayOf( uploader,
            title,
            date,
            price,
            offline,
            conferenceURL,
            content)

        return arrayOf(array)
    }
}