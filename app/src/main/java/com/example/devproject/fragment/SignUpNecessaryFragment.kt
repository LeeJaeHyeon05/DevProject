package com.example.devproject.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.devproject.R
import com.example.devproject.activity.account.LoginActivity
import com.example.devproject.databinding.FragmentSignUpNecessaryBinding
import com.example.devproject.util.DataHandler
import com.example.devproject.util.FirebaseIO
import com.example.devproject.util.UIHandler.Companion.signUpOptionalFragment

class SignUpNecessaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentSignUpNecessaryBinding.inflate(inflater, container, false)

        activity?.actionBar?.title = "회원가입 1/2"

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
                }else{
                    binding.TextLayoutPasswordConfirm.helperText = null
                }
            }

        })

        binding.signUpNextButton.setOnClickListener {

            if(binding.TvFieldInputPassword.text.length > 20){
                Toast.makeText(this.context, "비밀번호는 20자리 이하여야 합니다", Toast.LENGTH_SHORT).show()
            }
            else if(binding.TvFieldInputEmail.text!!.isNotEmpty() && binding.TvFieldInputPassword.text!!.isNotEmpty()
                && binding.TvFieldInputId.text!!.isNotEmpty() && binding.TvFieldInputPasswordConfirm.text!!.isNotEmpty()
                && binding.TextLayoutId.helperText == null && binding.TextLayoutEmail.helperText == null
                && binding.TextLayoutPassword.helperText == null && binding.TextLayoutPasswordConfirm.helperText == null){
                FirebaseIO.read("UserInfo", binding.TvFieldInputId.text.toString())
                    .addOnSuccessListener {
                        if(it.data != null){
                            Toast.makeText(this.context, "중복된 아이디입니다", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            DataHandler.signUpUserInfo.id = binding.TvFieldInputId.text.toString()
                            DataHandler.signUpUserInfo.Email = binding.TvFieldInputEmail.text.toString()

                            val password : String = binding.TvFieldInputPassword.text.toString()
                            setFragmentResult("requestKey", bundleOf("bundleKey" to password))

                            activity?.supportFragmentManager?.beginTransaction()
                                ?.replace(R.id.fragmentContainerView, signUpOptionalFragment)
                                ?.commit()

//                            auth.createUserWithEmailAndPassword(binding.TvFieldInputEmail.text.toString(), binding.TvFieldInputPassword.text.toString())
//                                .addOnCompleteListener(this){ task ->
//                                    if(task.isSuccessful){
//                                        val userInfo = UserInfo(
//                                            uid = auth.uid,
//                                            id = binding.TvFieldInputId.text.toString(),
//                                            Email = binding.TvFieldInputEmail.text.toString(),
//                                            language = adapter.getLanguageList(),
//                                            gitLink = binding.gitLinkEditText.text.toString(),
//                                        )
//                                        FirebaseIO.write("UserInfo", binding.TvFieldInputId.text.toString(), userInfo)
//
//                                        Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
//                                        val mIntent = Intent(this, LoginActivity::class.java)
//                                        mIntent.putExtra("LoginId", binding.TvFieldInputEmail.text.toString())
//                                        mIntent.putExtra("LoginPassword", binding.TvFieldInputPassword.text.toString())
//                                        setResult(AppCompatActivity.RESULT_OK, mIntent)
//                                        finish()
//                                    }
//                                    else{
//                                        Toast.makeText(this, "${task.exception?.message}", Toast.LENGTH_LONG).show()
//                                    }
//                                }
                        }
                    }
            }
            else{
                if(binding.TvFieldInputId.text == null || binding.TextLayoutId.helperText != null){
                    Toast.makeText(this.context, "아이디가 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputEmail.text == null || binding.TextLayoutEmail.helperText != null){
                    Toast.makeText(this.context, "이메일이 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else if(binding.TvFieldInputPassword.text == null || binding.TextLayoutPassword.helperText != null){
                    Toast.makeText(this.context, "비밀번호가 입력되지 않았거나 입력이 잘못되었습니다", Toast.LENGTH_SHORT).show()
                }
                else if (binding.TextLayoutPasswordConfirm.helperText != null){
                    Toast.makeText(this.context, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this.context, "공란이 있습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }

}