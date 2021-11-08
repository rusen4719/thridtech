package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import org.techtown.thridtech.databinding.ActivityChangeMyProfileBinding
import org.techtown.thridtech.databinding.ActivityFriendsAddBinding
import org.techtown.thridtech.databinding.ActivityShowMyProfileBinding

private var mBinding: ActivityShowMyProfileBinding? = null
private val binding get() = mBinding!!

class ShowMyProfile : AppCompatActivity() {
    val url = "https://chatdemo2121.herokuapp.com/"

    var myId = Preferences.prefs.getString("MyID", "null")
    val myName = Preferences.prefs.getString(myId+"_name", null.toString())
    val myStatus = Preferences.prefs.getString("MyStatus", null.toString())
    val myUrl = Preferences.prefs.getString(myId+"_url", null.toString())

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

        Glide.with(applicationContext).load(myUrl).into(binding.image)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val myName = Preferences.prefs.getString(myId+"_name", null.toString())
        val myStatus = Preferences.prefs.getString("MyStatus", null.toString())
        val myUrl = Preferences.prefs.getString(myId+"_url", null.toString())
        Glide.with(applicationContext).load(myUrl).into(binding.image)
        binding.profileName.text = myName
        binding.profileStatus.text = myStatus
    }
}