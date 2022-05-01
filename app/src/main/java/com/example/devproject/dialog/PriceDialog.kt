package com.example.devproject.dialog

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.example.devproject.databinding.ActivityDialogPriceBinding

class PriceDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var listener: PriceDialogClickedListener
    private lateinit var binding: ActivityDialogPriceBinding

    fun priceDia() {

        binding = ActivityDialogPriceBinding.inflate(dialog.layoutInflater)

        dialog.show()
        dialog.setContentView(binding.root)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val addPrice = binding.addPrice
        val finishBtn = binding.cancelBtn
        val continueBtn = binding.addBtn


        continueBtn.setOnClickListener {
            val priceNum = addPrice.text.toString()
            listener.onClicked(priceNum)

            dialog.dismiss()
        }
        finishBtn.setOnClickListener {
            dialog.dismiss()
        }

    }
    fun setOnOkClickedListener(listener: (String) -> Unit){
        this.listener = object: PriceDialogClickedListener {
            override fun onClicked(content: String) {
                listener(content)
            }
        }
    }

    interface PriceDialogClickedListener{
        fun onClicked(content: String)
    }
}