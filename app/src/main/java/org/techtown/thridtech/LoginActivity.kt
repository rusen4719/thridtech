package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
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

private var mBinding: ActivityLoginBinding? = null
private val binding get() = mBinding!!

class LoginActivity : AppCompatActivity() {

    val url = "https://chatdemo2121.herokuapp.com"

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
                        Log.d("TAG", response.body()?.data?.get("user_id").toString())
                        Log.d("TAG", response.body()?.msg.toString())
                        Log.d("TAG", response.toString())

                        startActivity(intent)

                        var myId = response.body()?.data?.toString()

                        val first = myId?.indexOf("user_id")?.plus(10)
                        val last = myId?.indexOf("password")?.minus(3)
                        myId = myId?.substring(first!!, last!!)

                        Log.d("TAG", myId.toString())
                        Log.d("TAG", response.body()?.data?.get("user_id")!!.asString)

                        var jsonInfo = JsonObject()
                        jsonInfo.addProperty("id", myId)

                        server?.findFriends(myId.toString())?.enqueue(object :Callback<FindFriend>{
                            override fun onResponse(call: Call<FindFriend>, response: Response<FindFriend>) {

                                Log.d("TAG", response.body().toString())
                                Log.d("TAG", response.body()?.data?.get(0).toString())
                                Log.d("TAG", response.body()?.msg.toString())
                            }

                            override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                                Log.d("TAG", "Fail to Find Friends")
                            }
                        })

                    }

                    override fun onFailure(call: Call<Login>, t: Throwable) {
                        Log.d("TAG", "fail")
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