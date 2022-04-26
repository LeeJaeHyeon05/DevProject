package com.example.devproject

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import com.example.devproject.databinding.ActivityLoginBinding
import com.example.devproject.databinding.DialogFindPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var getResultLoginInfo: ActivityResultLauncher<Intent>
    private lateinit var keyboardVisibilityUtils: KeyboardVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //나이트모드 적용 해제

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keyboardVisibilityUtils = KeyboardVisibilityUtils(window,
            onShowKeyboard = {keyboardHeight ->
                binding.loginScrollView.run {
                    smoothScrollBy(scrollX, scrollY)
                }
            })

        auth = FirebaseAuth.getInstance()

        autoLoginValidate() //자동 로그인

        getResultLoginInfo = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                binding.EtLoginId.setText(result.data?.getStringExtra("LoginId"))
                binding.EtLoginPassword.setText(result.data?.getStringExtra("LoginPassword"))
            }
        } //회원가입하고 로그인창에 입력한 아이디와 비밀번호를 가져오기 위한 런처

        binding.BtnLogin.setOnClickListener {
            loginProcess()
        }

        binding.BtnFindPassword.setOnClickListener {
            showDialog()
        }

        binding.BtnSignUp.setOnClickListener {
            val mIntent = Intent(this, SignUpActivity::class.java)
            getResultLoginInfo.launch(mIntent)
        }
    }

    override fun onStop() {
        super.onStop()

        val savePref = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE)
        savePref.edit().putBoolean("CheckBox", binding.CheckboxAutoLogin.isChecked).apply()
        savePref.edit().putString("Email", binding.EtLoginId.text.toString()).apply()
    }

    private fun autoLoginValidate(){
        val sharedPref = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE).getBoolean("CheckBox", false)
        val sharedId = getSharedPreferences("saveAutoLoginChecked", MODE_PRIVATE).getString("Email", null)

        if (sharedId != null && sharedPref) {
            auth.currentUser?.reload()?.addOnCompleteListener { task -> //자동로그인시 계정이 정지되었는지 삭제되었는지 확인
                if(task.isSuccessful){
                    Toast.makeText(this, "자동로그인 되었습니다", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this, "사용자 계정이 정지되었거나 삭제되었습니다. 관리자에게 문의하세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginProcess(){
        val email = binding.EtLoginId.text.toString()
        val password = binding.EtLoginPassword.text.toString()
        if(email.isNotEmpty()&&password.isNotEmpty()){
            DataHandler.load()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){ task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity, "등록되지 않은 계정이거나 비밀번호가 올바르지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            DataHandler.delete()
            Toast.makeText(this, "이메일 또는 비밀번호가 입력되지 않았습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(){
        val dialogBinding: DialogFindPasswordBinding = DialogFindPasswordBinding.inflate(LayoutInflater.from(this))

        var builder = AlertDialog.Builder(this)
        var ad = builder.create()

        ad.setView(dialogBinding.root)
        ad.setTitle("비밀번호 찾기")

        dialogBinding.BtnFindPasswordDialogOk.setOnClickListener {
            var email = dialogBinding.EtFindPasswordEmail.text.toString()

            if(email.isNotEmpty()){
                auth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "입력한 이메일로 재설정 링크를 보냈습니다. 이메일을 확인해 주세요", Toast.LENGTH_SHORT).show()
                        ad.dismiss()
                    }
                    else{
                        Toast.makeText(this, "등록되지 않은 이메일입니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
        }

        dialogBinding.BtnFindPasswordDialogCancel.setOnClickListener {
            ad.dismiss()
        }

        ad.show()
    }
}