package com.example.devproject.format

data class UserInfo(
    var uid: String? = null,
    var id: String? = null,
    var Email: String? = null,
    var language : MutableList<String>? = null,
    var gitLink : String = "",
    var position : Long? = 0
)