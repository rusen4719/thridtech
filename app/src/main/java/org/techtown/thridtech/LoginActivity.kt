package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.techtown.thridtech.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url

private var mBinding: ActivityLoginBinding? = null
private val binding get() = mBinding!!

class LoginActivity : AppCompatActivity() {

    val url = "https://chatdemo2121.herokuapp.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Thridtech)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(!Preferences.prefs.getString("MyID","").isNullOrEmpty()) {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginMainLayout.setOnClickListener(){
            hideKeyboard()
        }
        binding.loginScrollLayout.setOnClickListener {
            hideKeyboard()
        }

        binding.loginBtnCreateAccount.setOnClickListener{
            val intent = Intent(this, create_account::class.java)
            startActivity(intent)
        }

        binding.loginFindAccount.setOnClickListener {
            val intent = Intent(this, find_account::class.java)
            startActivity(intent)
        }

        binding.loginBtnSignIn.setOnClickListener {

            val edtId = binding.loginEdtAddress
            val edtPwd = binding.loginEdtPassword

            var layoutInflater = LayoutInflater.from(applicationContext).inflate(R.layout.view_holder_toast,null)
            var text : TextView = layoutInflater.findViewById(R.id.textViewToast)

            if(edtId.text.isNullOrEmpty() || edtPwd.text.isNullOrEmpty()) {
                text.text="입력정보를 다시 확인해주세요."

                var toast = Toast(this)
                toast.view = layoutInflater
                toast.setGravity(0,0,0)
                toast.show()
            } else {
                val intent = Intent(this, Home::class.java)
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                var server = retrofit?.create(APIInterface::class.java)

                var jsonObj = JsonObject()
                jsonObj.addProperty("user_id", edtId.text.toString())
                jsonObj.addProperty("password", edtPwd.text.toString())

                server?.login(jsonObj)?.enqueue(object :Callback<Login>{
                    override fun onResponse(call: Call<Login>, response: Response<Login>) {
                        binding.loginEdtAddress.text?.clear()
                        binding.loginEdtPassword.text?.clear()

                        startActivity(intent)

                        var myId = response.body()?.data?.get("user_id")!!.asString
                        var myName = response.body()?.data?.get("name")!!.asString
                        var myStatus = response.body()?.data?.get("status_msg")!!.asString
                        var myUrl = response.body()?.data?.get("profile_img_url")!!.asString
                        var myObjectID = response.body()?.data?.get("_id")!!.asString

                        Preferences.prefs.setString("MyID", myId.toString())
                        Preferences.prefs.setString(myId+"_name", myName.toString())
                        Preferences.prefs.setString("MyStatus", myStatus.toString())
                        if(myUrl.isNullOrEmpty()) {
                            Preferences.prefs.setString(myId+"_url", "http://i.ibb.co/2d9vT7F/sample-1.jpg")
                        } else {
                            Preferences.prefs.setString(myId+"_url", myUrl.toString())
                        }

                        Log.d("TAG", "url : " + URLUtil.isValidUrl("http:://www.naver.com").toString())

                        Preferences.prefs.setString("MyObjectId", myObjectID.toString())
                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        text.text="입력정보를 다시 확인해주세요."

                        var toast = Toast(applicationContext)
                        toast.view = layoutInflater
                        toast.setGravity(0,0,0)
                        toast.show()
                    }
                })
            }
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