package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import org.techtown.thridtech.databinding.ActivityChangeMyProfileBinding
import org.techtown.thridtech.databinding.ActivityFriendsAddBinding
import org.techtown.thridtech.databinding.ActivityShowMyProfileBinding

private var mBinding: ActivityShowMyProfileBinding? = null
private val binding get() = mBinding!!

class ShowMyProfile : AppCompatActivity() {
    val url = "https://chatdemo2121.herokuapp.com/"

    val myName = Preferences.prefs.getString("MyName", null.toString())
    val myStatus = Preferences.prefs.getString("MyStatus", null.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_change_my_profile)
        mBinding = ActivityShowMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.profileName.text = myName
        binding.profileStatus.text = myStatus

        binding.btnChangeProfile.setOnClickListener {
            val intent = Intent(applicationContext, ChangeMyProfile::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}