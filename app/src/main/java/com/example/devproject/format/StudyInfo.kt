package com.example.devproject.format

import android.net.Uri

data class StudyInfo (
    var documentID : String? = null,
    var ongoing : Boolean? = null,
    var title : String? = null,
    var content : String? = null,
    var offline : Boolean? = null,
    var studyURL : String? = null,
    var totalMember : Long? = 0,
    var remainingMemeber : Long? = 0,
    val language: MutableList<String>? = null,
    var uid : String? = null,
    var uploader : String? = null,
    var endDate : String? = null
)