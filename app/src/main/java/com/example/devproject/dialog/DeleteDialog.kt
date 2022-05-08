package com.example.devproject.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.devproject.databinding.ActivityDialogDefaultBinding

class DeleteDialog(context: Context) {

    private val dialog = Dialog(context)
    var okButton : Button?= null
    private lateinit var binding: ActivityDialogDefaultBinding

    fun deleteDialog() {
        binding = ActivityDialogDefaultBinding.inflate(dialog.layoutInflater)
        dialog.show()
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        var defaultDialogTextView : TextView = binding.defaultDialogTextView
        defaultDialogTextView.text = "정말 삭제하시겠습니까?"
        okButton = binding.defaultDialogOkButton
        val cancelButton = binding.defaultDialogCancelButton
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}