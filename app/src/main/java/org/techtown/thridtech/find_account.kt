package org.techtown.thridtech

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.techtown.thridtech.databinding.ActivityCreateAccountBinding
import org.techtown.thridtech.databinding.ActivityFindAccountBinding

// 전역 변수로 바인딩 객체 선언
private var mBinding: ActivityFindAccountBinding? = null
// 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
private val binding get() = mBinding!!

var selected = 0

class find_account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_find_account)
        mBinding = ActivityFindAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btn_find_id = binding.btnFindId
        val btn_find_pwd = binding.btnFindPwd

        val txt_show_data = binding.txtShowData

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnFindId.setOnClickListener {
            selected = 0

            btn_find_id.setBackgroundColor(Color.parseColor("#696969"))
            btn_find_id.setTextColor(Color.parseColor("#ffffff"))
            btn_find_pwd.setBackgroundColor(Color.parseColor("#CDCDCD"))
            btn_find_pwd.setTextColor(Color.parseColor("#949494"))

            binding.findEmail.visibility = View.VISIBLE
            binding.findPwd.visibility = View.GONE
            binding.layoutShowData.visibility = View.GONE

            binding.btnFindIdOk.text = "이메일 찾기"
        }

        binding.btnFindPwd.setOnClickListener {
            selected = 1

            btn_find_pwd.setBackgroundColor(Color.parseColor("#696969"))
            btn_find_pwd.setTextColor(Color.parseColor("#ffffff"))
            btn_find_id.setBackgroundColor(Color.parseColor("#CDCDCD"))
            btn_find_id.setTextColor(Color.parseColor("#949494"))

            binding.findEmail.visibility = View.INVISIBLE
            binding.findPwd.visibility = View.VISIBLE
            binding.layoutShowData.visibility = View.GONE

            binding.btnFindIdOk.text = "비밀번호 찾기"
        }

        binding.copyShowData.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("copy_data", txt_show_data.text.toString())
            clipboardManager!!.setPrimaryClip(clipData)

            Toast.makeText(this, "복사되었습니다.", Toast.LENGTH_SHORT).show()

        }

        binding.btnFindIdOk.setOnClickListener {

            if (selected == 0 ){
                binding.copyShowData.visibility = View.VISIBLE
            } else {
                binding.copyShowData.visibility = View.GONE
            }
            binding.layoutShowData.visibility = View.VISIBLE

        }

    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}