package com.example.devproject.activity.account

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.devproject.R
import com.example.devproject.databinding.FragmentHeadhuntingBinding
import com.example.devproject.databinding.FragmentSignUpNecessaryBinding
import com.example.devproject.util.UIHandler.Companion.signUpOptionalFragment

class SignUpNecessaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignUpNecessaryBinding.inflate(inflater, container, false)

        binding.TvFieldInputId.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutId.helperText = "아이디를 비울 수 없습니다"
                }
                if(InputString.toString().isNotEmpty()){
                    binding.TextLayoutId.helperText = null
                }

                if(InputString.toString().contains(" ")){
                    binding.TextLayoutId.helperText = "띄어쓰기는 허용되지 않습니다"
                }
            }
        })

        binding.TvFieldInputEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val email = s.toString()
                val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if(!isValid){
                    binding.TextLayoutEmail.helperText = "올바른 이메일은 형식은 abc@example.com 입니다"
                }
                else{
                    binding.TextLayoutEmail.helperText = null
                }
            }

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutEmail.helperText = "이메일을 비울 수 없습니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutEmail.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(InputString).matches()){
                    binding.TextLayoutEmail.helperText = "올바른 이메일은 형식은 abc@example.com 입니다"
                }
                else{
                    binding.TextLayoutEmail.helperText = null
                }
            }

        })

        binding.TvFieldInputPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, count: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutPassword.helperText = "비밀번호를 비울 수 없습니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPassword.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else{
                    binding.TextLayoutPassword.helperText = null
                }
                if(binding.TvFieldInputPasswordConfirm.text.toString() == InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
                if(InputString!!.length < 6 && InputString!!.length < 21){
                    binding.TextLayoutPassword.helperText = "비밀번호는 6자리 이상 20자리 이하여야합니다"
                }
            }
        })

        binding.TvFieldInputPasswordConfirm.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(InputString: Editable?) {
                if(InputString.toString().isEmpty()){
                    binding.TextLayoutPasswordConfirm.helperText = "비밀번호를 재입력 해야합니다"
                }
                else if(InputString.toString().contains(" ")){
                    binding.TextLayoutPasswordConfirm.helperText = "띄어쓰기는 허용되지 않습니다"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }

                if(binding.TvFieldInputPassword.text.toString() != InputString.toString()){
                    binding.TextLayoutPasswordConfirm.helperText = "비밀번호가 일치하지 않습니다"
                }
                else{
                    binding.TextLayoutPasswordConfirm.helperText = null

                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.fragmentContainerView, signUpOptionalFragment)
                        ?.commit()
                }
            }

        })


        return binding?.root
    }

}