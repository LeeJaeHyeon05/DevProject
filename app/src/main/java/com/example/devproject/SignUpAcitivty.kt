package com.example.devproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import com.example.devproject.databinding.ActivitySignUpAcitivtyBinding

class SignUpAcitivty : AppCompatActivity() {

    lateinit var binding: ActivitySignUpAcitivtyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpAcitivtyBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.TvFieldInputId.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TvFieldInputId.error = "아이디를 비울 수 없습니다."
                }
                if(InputString.toString().contains(" ")){
                    binding.TvFieldInputId.error = "띄어쓰기는 허용되지 않습니다"
                }
            }
        })
    }
}