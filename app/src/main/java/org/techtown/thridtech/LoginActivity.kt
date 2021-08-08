package org.techtown.thridtech

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import org.techtown.thridtech.databinding.ActivityLoginBinding
import org.techtown.thridtech.databinding.ActivityMainBinding

// 전역 변수로 바인딩 객체 선언
private var mBinding: ActivityLoginBinding? = null
// 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
private val binding get() = mBinding!!


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginMainLayout.setOnClickListener(){
            hideKeyboard()
        }
        binding.loginScrollLayout.setOnClickListener {
            hideKeyboard()
        }

    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.loginEdtAddress.windowToken, 0)
        binding.loginEdtAddress.clearFocus()
        binding.loginEdtPassword.clearFocus()
    }


    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}