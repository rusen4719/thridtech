package org.techtown.thridtech

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.StringBuilderPrinter
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonObject
import org.techtown.thridtech.databinding.ActivityCreateAccountBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URISyntaxException
import kotlin.reflect.typeOf

private var mBinding: ActivityCreateAccountBinding? = null
private val binding get() = mBinding!!

class create_account : AppCompatActivity() {

    val url = "https://chatdemo2121.herokuapp.com/"

    var passOverlap = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_create_account)
        mBinding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnOverlap.setOnClickListener {
            val address = binding.edtAddress.text.toString()

            Log.d("TAG", address)

            var layoutInflater = LayoutInflater.from(applicationContext).inflate(R.layout.view_holder_toast,null)
            var text : TextView = layoutInflater.findViewById(R.id.textViewToast)

            if(address.trim().equals("") || address.trim().length < 4) {
                text.text = "4자 이상 입력해주세요"
                var toast = Toast(applicationContext)
                toast.view = layoutInflater
                toast.setGravity(0,0,0)
                toast.show()

                passOverlap = 0
            } else {
                Log.d("TAG", "1")
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                var server = retrofit?.create(APIInterface::class.java)

                var jsonInfo = JsonObject()
                jsonInfo.addProperty("user_id", address)

                server?.overlap(jsonInfo)?.enqueue(object :Callback<OverlapID>{
                    override fun onResponse(call: Call<OverlapID>, response: Response<OverlapID>) {
                        if (response.body()?.data!!) {
                            Log.d("TAG", response.body()?.msg.toString())
                            Log.d("TAG", response.body()?.data.toString())
                            Log.d("TAG", response.toString())

                            text.text= "사용 가능합니다."

                            var toast = Toast(applicationContext)
                            toast.view = layoutInflater
                            toast.setGravity(0,0,0)
                            toast.show()

                            passOverlap = 1
                        } else {
                            text.text= "이미 있는 아이디 입니다."

                            var toast = Toast(applicationContext)
                            toast.view = layoutInflater
                            toast.setGravity(0,0,0)
                            toast.show()

                            passOverlap = 0
                        }

                    }

                    override fun onFailure(call: Call<OverlapID>, t: Throwable) {
                        Log.d("TAG", t.toString())
                    }
                })
            }
        }

        binding.btnCreateAccount.setOnClickListener{
            val name = binding.edtName.text.toString().replace(" ", "").equals("")
            val address = binding.edtAddress.text.toString().replace(" ", "").equals("")
            val password = binding.edtPassword.text.toString()
            val password_confirm = binding.edtPasswordConfirm.text.toString()
            val phone_number = binding.edtPhone.text.toString().replace(" ", "").equals("")

            var layoutInflater = LayoutInflater.from(this).inflate(R.layout.view_holder_toast,null)
            var text : TextView = layoutInflater.findViewById(R.id.textViewToast)

            if (name || address || phone_number || password.replace(" ", "").equals("")
                || password_confirm.replace(" ", "").equals("") || password != password_confirm
                || passOverlap == 0) {

                text.text="입력정보를 다시 확인해주세요."

                var toast = Toast(this)
                toast.view = layoutInflater
                toast.setGravity(0,0,0)
                toast.show()
            } else {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                var server = retrofit?.create(APIInterface::class.java)

                var jsonInfo = JsonObject()
                jsonInfo.addProperty("createUID", binding.edtAddress.text.toString())
                jsonInfo.addProperty("createPWD", binding.edtPassword.text.toString())
                jsonInfo.addProperty("createNM", binding.edtName.text.toString())
                jsonInfo.addProperty("createTLP", binding.edtPhone.text.toString())

                server?.createAccount(jsonInfo)?.enqueue(object :Callback<CreateID>{
                    override fun onResponse(call: Call<CreateID>, response: Response<CreateID>) {

                        if(response.body()?.data!!) {
                            text.text = "회원가입이 완료 되었습니다."

                            Log.d("TAG", response.body()?.msg.toString())

                            var toast = Toast(applicationContext)
                            toast.view = layoutInflater
                            toast.setGravity(0,0,0)
                            toast.show()

                            passOverlap = 0

                            Handler().postDelayed({ startActivity(Intent(applicationContext
                                , LoginActivity::class.java)) }, 2000)

                        } else {
                            text.text = "입력정보를 다시 확인해주세요."

                            Log.d("TAG", response.body()?.msg.toString())

                            var toast = Toast(applicationContext)
                            toast.view = layoutInflater
                            toast.setGravity(0,0,0)
                            toast.show()

                        }
                    }

                    override fun onFailure(call: Call<CreateID>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        binding.edtPasswordConfirm.addTextChangedListener(object : TextWatcher {
            val password = binding.edtPassword
            val password_confirm = binding.edtPasswordConfirm
            val confirm_error = binding.txtConfirmError

            override fun afterTextChanged(p0: Editable?) {
                if (password.text.toString() == p0.toString()) {
                    confirm_error.visibility = View.INVISIBLE
                } else {
                    confirm_error.visibility = View.VISIBLE
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}