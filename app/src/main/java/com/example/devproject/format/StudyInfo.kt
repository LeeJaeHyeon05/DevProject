package com.example.devproject.format

data class StudyInfo (
    var ongoing : Boolean? = null,
    var title : String? = null,
    var content : String? = null,
    var offline : Boolean? = null,
    var studyURL : String? = null,
    var totalMember : Long? = 0,
    var remainingMemeber : Long? = 0
)