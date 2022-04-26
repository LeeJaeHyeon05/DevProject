package com.example.devproject.AddConferences

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.devproject.R

class PriceDialog(context: Context) {
    private val dialog = Dialog(context)

    fun priceDia() {
        dialog.show()
        dialog.setContentView(R.layout.activity_dialog_price)
        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val addPrice = dialog.findViewById<EditText>(R.id.addPrice)
        val finishBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val continueBtn = dialog.findViewById<Button>(R.id.addBtn)
        val mainTV = dialog.findViewById<TextView>(R.id.priceTextView)

        continueBtn.setOnClickListener {
            val priceNum = addPrice.text.toString()
            mainTV.text = priceNum
            dialog.dismiss()
        }
        finishBtn.setOnClickListener {
            dialog.dismiss()
        }

    }
}