package com.example.devproject.chat

class ChatModel(
    var users: HashMap<String, Boolean>,
    var comments: Map<String, Comment>,
)
class Comment(
    var uid: String? = null,
    var message: String? = null
)
