package org.techtown.thridtech

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import org.techtown.thridtech.databinding.ActivityChattingBinding
import org.techtown.thridtech.databinding.ActivityMakeChatRoomBinding
import android.view.MotionEvent

import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToLong
import kotlin.reflect.typeOf

private var mBinding: ActivityChattingBinding? = null
private val binding get() = mBinding!!

class Chatting : AppCompatActivity() {
    val url = "https://chatdemo2121.herokuapp.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server = retrofit?.create(APIInterface::class.java)

    lateinit var adapter: Adapter_Chatting
    val datas = mutableListOf<Data_chatting_basic>()

    var showPlayer = 0

    var room_id : String? = null

    lateinit var mSocket: Socket

    val myId = Preferences.prefs.getString("MyID" , "null")
    val myUrl = Preferences.prefs.getString(myId+"_url" , "null")
    val myName = Preferences.prefs.getString("MyName" , "null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chatting)
        mBinding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            mSocket = IO.socket(url)
            mSocket.connect()
        } catch (e : URISyntaxException) { }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.on("message", onMessage)

        adapter = Adapter_Chatting(this)
        binding.recycler.setHasFixedSize(true)

        if (intent.hasExtra("title")) {
            binding.title.text = intent.getStringExtra("title")
        }

        if (intent.hasExtra("room_id")) {
            room_id = intent.getStringExtra("room_id")
        }

        callChats()

        binding.btnBack.setOnClickListener { finish() }

        binding.textInputLayout.setEndIconOnClickListener {
            var chatting = binding.edtChatting.text.toString()

            var messageObj = JsonObject()
            messageObj.addProperty("room_id", room_id)
            messageObj.addProperty("send_user_id", myId)
            messageObj.addProperty("message", chatting)
            messageObj.addProperty("not_read", 1)

            mSocket.emit("message", messageObj)

            var posixTime = System.currentTimeMillis()
            val date = Date(posixTime)
            val mForamt = SimpleDateFormat("hh시 mm분")
            val time = mForamt.format(date)

            datas.apply {
                add(Data_chatting_basic(name = myName, contents = chatting
                    , timedate = time, type = multi_type1, image = myUrl))
            }

            adapter.datas = datas
            adapter.notifyDataSetChanged()
            binding.recycler.adapter = adapter

            binding.edtChatting.setText("")
        }

        binding.btnShowPlayer.setOnClickListener {
            if (showPlayer == 0 ) {
                binding.btnShowPlayer.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                binding.youtubePlayer.bringToFront()
                binding.youtubePlayer.isVisible = true
                showPlayer = 1
            } else if (showPlayer == 1) {
                binding.btnShowPlayer.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                binding.youtubePlayer.isVisible = false
                showPlayer = 0
            }
        }
    }

    val onConnect = Emitter.Listener {
        Log.d("TAG", "Chatting.kt : 연결됨")
    }

    val onMessage = Emitter.Listener {
        Log.d("TAG", "onMessage : " + it.toString())
    }

    fun callChats() {
        var posixTime = System.currentTimeMillis()

        server?.callChats(room_id!!, posixTime.toString())?.enqueue(object : Callback<CallChats> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<CallChats>, response: Response<CallChats>) {
                var type : Int? = null

                val myId = Preferences.prefs.getString("MyID","null")

/*                Log.d("TAG", "id : "+ it.getId())
                Log.d("TAG", "room_id : "+ it.room_id)
                Log.d("TAG", "message : "+ it.message)
                Log.d("TAG", "send_user_id : "+ it.send_user_id)
                Log.d("TAG", "createdat : "+ it.createdat)*/

                response.body()?.data?.forEach {
                    Log.d("TAG", "object ; " + it.getId())
                    if (it.send_user_id == myId){
                        type = multi_type2
                    } else {
                        type = multi_type1
                    }
                    val date = Date(it.createdat!!.toLong() * 1000L)
                    val mForamt = SimpleDateFormat("hh시 mm분")
                    val time = mForamt.format(date)
                    val url = Preferences.prefs.getString(it.send_user_id+"_url","null")
                    datas.apply {
                        add(Data_chatting_basic(name = it.send_user_id.toString(), contents = it.message.toString()
                            , timedate = time, type = type!!, image = url))
                    }
                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                    binding.recycler.adapter = adapter
                }

            }

            override fun onFailure(call: Call<CallChats>, t: Throwable) {
                Log.d("TAG", t.toString())
            }
        })
    }

    fun findFriedsDetail(id : String) {
        server?.findFriends(id)?.enqueue(object : Callback<FindFriend> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<FindFriend>, response: Response<FindFriend>) {
                Log.d("TAG", "findFriedsDetail: " + response.body()?.data.toString())

            }

            override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                Log.d("TAG", t.toString())
            }
        })
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}