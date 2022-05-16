package com.example.devproject.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.devproject.R
import com.example.devproject.databinding.DialogFilterBinding
import com.example.devproject.util.DataHandler

class FilterDialog(context: Context) {

    private val dialog = Dialog(context)
    var okButton : Button?= null
    private lateinit var binding: DialogFilterBinding

    fun activate() {
        binding = DialogFilterBinding.inflate(dialog.layoutInflater)
        dialog.show()
        dialog.setContentView(binding.root)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        okButton = binding.filterDialogOkButton

        val filterPriceRadioGroup = binding.filterPriceRadioGroup
        filterPriceRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            when(id){
                R.id.priceTrueRadioButton -> DataHandler.filterList[0] = 1
                R.id.priceFalseRadioButton -> DataHandler.filterList[0] = 0
            }
        }
        val cancelButton = binding.filterDialogCancelButton
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun dismiss(){
        dialog.dismiss()
    }
}