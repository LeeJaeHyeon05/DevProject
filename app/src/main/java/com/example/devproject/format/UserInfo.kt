package com.example.devproject.format

data class UserInfo(
    var uid: String? = null,
    var id: String? = null,
    var Email: String? = null,
    var mainLanguage : String? = "",
    var languages : MutableList<String>? = null,
    var position : String? =""
)