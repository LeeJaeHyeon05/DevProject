package com.example.devproject.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.devproject.databinding.DialogDefaultBinding

class BasicDialog(context : Context, content : String) {
    private val dialog = Dialog(context)
    var okButton : Button?= null
    var content = content
    private lateinit var binding: DialogDefaultBinding

    fun activate() {
        binding = DialogDefaultBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        var defaultDialogTextView : TextView = binding.defaultDialogTextView
        defaultDialogTextView.text = content
        okButton = binding.defaultDialogOkButton
        val cancelButton = binding.defaultDialogCancelButton
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}