package com.example.devproject.addConferences

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.OnReceiveContentListener
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.devproject.R

class PriceDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var listener: PriceDialogClickedListener

    fun priceDia() {
        dialog.show()
        dialog.setContentView(R.layout.activity_dialog_price)

        dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val addPrice = dialog.findViewById<EditText>(R.id.addPrice)
        val finishBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val continueBtn = dialog.findViewById<Button>(R.id.addBtn)


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
        this.listener = object: PriceDialogClickedListener{
            override fun onClicked(content: String) {
                listener(content)
            }
        }
    }

    interface PriceDialogClickedListener{
        fun onClicked(content: String)
    }
}