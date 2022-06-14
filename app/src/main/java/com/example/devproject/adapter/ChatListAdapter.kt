package com.example.devproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.chat.ChatActivity
import com.example.devproject.databinding.ChatListItemBinding
import com.example.devproject.util.DataHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.chat_list_item.view.*

class ChatListAdapter(val list: ArrayList<String>): RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ChatListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(name: String){
            binding.chatTextView.text = name
        }

        fun onclick(){
            binding.root.setOnClickListener {
                it.context.startActivity(Intent(it.context, ChatActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.onclick()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}