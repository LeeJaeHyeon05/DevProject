package com.example.devproject.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devproject.R
import com.example.devproject.adapter.ChatListAdapter
import com.example.devproject.chat.ChatModel
import com.example.devproject.databinding.DialogFindPasswordBinding
import com.example.devproject.databinding.FragmentChatBinding
import com.example.devproject.format.UserInfo
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.chat_list_item.view.*
import kotlinx.android.synthetic.main.dialog_find_password.*


class ChatFragment : Fragment() {

    private var mbinding: FragmentChatBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        UIHandler.mainActivity?.supportActionBar?.title = "채팅"

        val binding = FragmentChatBinding.inflate(inflater, container, false)
        mbinding = binding

        val chatList = ArrayList<String>()

        val currentId = DataHandler.userInfo.id
        if (currentId != null) {
            FirebaseDatabase.getInstance("https://deverence-2022-default-rtdb.asia-southeast1.firebasedatabase.app")
                .reference
                .child("user")
                .child(currentId)
                .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(i in snapshot.children){
                        val list = i.value
                        chatList.add(list as String)
                    }
                    var chatRecyclerView = mbinding?.chatListRecyclerView
                    chatRecyclerView?.layoutManager = LinearLayoutManager(context)
                    chatRecyclerView?.adapter = ChatListAdapter(chatList)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }



        val addChat = mbinding?.chatAddButton
        addChat?.setOnClickListener {
            val dialog = this.context?.let { it1 -> view?.let { it2 -> ChatCustomDialog(it1, it2) } }
            dialog?.showDialog()
        }


        return mbinding?.root
    }

    companion object {

    }
}

class ChatCustomDialog(context: Context, view: View){
    private val dialog = Dialog(context)
    private val view = view
    private var uid: String = ""
    private var destinationUid: String = ""
    private var chatRoomId: String = ""
    private var user: MutableMap<String, Boolean> = mutableMapOf()

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_find_password)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val editText = dialog.findViewById<EditText>(R.id.EtFindPasswordEmail)
        editText.hint = "아이디를 입력하세요"
        dialog.BtnFindPasswordDialogOk.setOnClickListener {
            val database = FirebaseDatabase.getInstance("https://deverence-2022-default-rtdb.asia-southeast1.firebasedatabase.app")

            database.reference.child("chatrooms").orderByChild("users/${editText.text}").equalTo(true)

            FirebaseIO.db.collection("UserInfo").document(editText.text.toString()).get()
                .addOnSuccessListener {

                    uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    destinationUid = it.data?.get("uid").toString()

                    val currentId = DataHandler.userInfo.id

                    user[currentId.toString()] = true
                    user[editText.text.toString()] = true

                    if(destinationUid != "null"){
                        database.reference.child("chatrooms").push().child("users").setValue(user)
                        if (currentId != null) {
                            database.reference.child("user").child(currentId).push().setValue(editText.text.toString())
                            database.reference.child("user").child(editText.text.toString()).push().setValue(currentId)
                        }
                    }
                    else{
                        Toast.makeText(this.dialog.context, "사용자를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            dialog.dismiss()
        }

        dialog.BtnFindPasswordDialogCancel.setOnClickListener {
            dialog.dismiss()
        }

    }

    fun checkChatRoom(otherPerson: String){
        FirebaseDatabase.getInstance("https://deverence-2022-default-rtdb.asia-southeast1.firebasedatabase.app")
            .reference.child("chatrooms").orderByChild("users/$otherPerson").equalTo(true)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(i in snapshot.children){
                        if(user.containsKey(otherPerson)){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}