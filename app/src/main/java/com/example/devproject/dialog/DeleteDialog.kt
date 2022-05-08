package com.example.devproject.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import com.example.devproject.databinding.ActivityDialogDeleteBinding
import com.example.devproject.databinding.ActivityDialogPriceBinding
import com.example.devproject.util.FirebaseIO

class DeleteDialog(context: Context) {

    private val dialog = Dialog(context)
    var okButton : Button?= null
    private lateinit var binding: ActivityDialogDeleteBinding


    fun deleteDialog() {
        binding = ActivityDialogDeleteBinding.inflate(dialog.layoutInflater)
        dialog.show()
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        okButton = binding.deleteOkBuitton
        val cancelButton = binding.deleteCancelButton
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }
}