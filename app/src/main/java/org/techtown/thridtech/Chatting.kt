package org.techtown.thridtech

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.core.view.isVisible
import org.techtown.thridtech.databinding.ActivityChattingBinding
import org.techtown.thridtech.databinding.ActivityMakeChatRoomBinding
import android.view.MotionEvent
import android.view.View

import android.view.View.OnTouchListener
import android.webkit.URLUtil
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToLong
import kotlin.reflect.typeOf

private var mBinding: ActivityChattingBinding? = null
private val binding get() = mBinding!!

class Chatting : YouTubeBaseActivity()  {
    val url = "https://chatdemo2121.herokuapp.com/"

    val REQ_GALLERY = 12
    val REQUEST_IMAGE_CAPTURE = 1

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var server = retrofit?.create(APIInterface::class.java)

    lateinit var adapter: Adapter_Chatting
    val datas = mutableListOf<Data_chatting_basic>()

    var showPlayer = 0

    var room_id : String? = null

    var selectUri : Uri? = null
    var input : String? = null
    var changeUrl :String? = null

    lateinit var mSocket: Socket

    val myId = Preferences.prefs.getString("MyID", "null")
    val myName = Preferences.prefs.getString("MyName", "null")
    val myUrl = Preferences.prefs.getString(myId+"_url", "null")
    var videoId = Preferences.prefs.getString("youtubeId", "null")

    var youtubeView : YouTubePlayerView? = null
    var youtubePlayer : YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_chatting)
        mBinding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        youtubeView = findViewById<YouTubePlayerView>(R.id.youtube_player)

        initPlayer()

        binding.btnShowPlayer.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
        binding.btnBack.setImageResource(R.drawable.ic_baseline_arrow_back_24)

        requestPermission()

        val highNoon = Calendar.getInstance()
        highNoon.set(Calendar.HOUR_OF_DAY,24)
        highNoon.set(Calendar.MINUTE,0)
        highNoon.set(Calendar.SECOND,1)

        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {

                var posixTime = System.currentTimeMillis()
                var date = Date(posixTime)
                var formatDate = SimpleDateFormat("yyyy년 MM월 dd일")
                var resultDate = formatDate.format(date)

                datas.apply {
                    add(Data_chatting_basic(name = "", contents = ""
                        , timedate = resultDate, type = multi_type3, image = ""))
                }
                adapter.datas = datas
                adapter.notifyDataSetChanged()
                binding.recycler.adapter = adapter
            }
        },highNoon.time)

        if (intent.hasExtra("title")) {
            binding.title.text = intent.getStringExtra("title")
        }

        if (intent.hasExtra("room_id")) {
            room_id = intent.getStringExtra("room_id")
        }

        try {
            mSocket = IO.socket(url)
        } catch (e : URISyntaxException) { }

        mSocket.on(Socket.EVENT_CONNECT, onConnect)
        mSocket.connect()
        mSocket.on("message", onMessage)

        mSocket.emit("join room", room_id)

        adapter = Adapter_Chatting(this)
        binding.recycler.setHasFixedSize(true)

        binding.recycler.scrollToPosition(adapter.datas.size - 1)

        callChats()

        binding.btnBack.setOnClickListener { finish() }

        binding.textInputLayout.setEndIconOnClickListener {
            var chatting = binding.edtChatting.text.toString()

            var messageObj = JSONObject()
            messageObj.put("room_id", room_id)
            messageObj.put("send_user_id", myId)
            messageObj.put("message", chatting)
            messageObj.put("not_read", 1)

            mSocket.emit("message", messageObj)

            var posixTime = System.currentTimeMillis()
            val date = Date(posixTime)
            val mForamt = SimpleDateFormat("hh시 mm분")
            val time = mForamt.format(date)

            binding.recycler.scrollToPosition(adapter.datas.size - 1)

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

        binding.galleryBtn.setOnClickListener {
            selectGallery()

        }
    }

    val onConnect = Emitter.Listener {
    }

    val onMessage = Emitter.Listener {
        val jsonObject =JSONObject(it.get(0).toString())
        val room_id = jsonObject.get("room_id")
        val send_user_id = jsonObject.get("send_user_id")
        val message = jsonObject.get("message")
        val time = jsonObject.get("createdAt")
        val name = Preferences.prefs.getString(send_user_id.toString()+"_name","null")
        val url = Preferences.prefs.getString(send_user_id.toString()+"_url","null")
        var type : Int? = null

        var showTime = sendTime(time.toString())

        if (send_user_id.toString() == myId) {
            type = multi_type2
        } else {
            type = multi_type1
        }

        if (message.toString().contains("youtube.com/watch")) {
            var youtubeId = message.toString().split("=").get(1)

            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).async {
                }.await()

                playVideo(youtubeId)

            }

        } else if (URLUtil.isValidUrl(message.toString())) {

            if (message.toString().contains("youtube.com/watch")) {

            } else {
                Log.d("TAG", "urluril : ")
                type = multi_type4
                showImage(name, message.toString(), showTime, type, url)
            }

        } else {
            CoroutineScope(Dispatchers.Main).launch {
                CoroutineScope(Dispatchers.Default).async {

                }.await()
                datas.apply {
                    add(Data_chatting_basic(name = name, contents = message.toString()
                        , timedate = showTime, type = type, image = url))
                }

                adapter.datas = datas
                adapter.notifyDataSetChanged()
                binding.recycler.adapter = adapter

                binding.recycler.scrollToPosition(adapter.datas.size - 1)
            }
        }
    }

    fun initPlayer() {
        youtubeView?.initialize("chatting", object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer, wasRestored: Boolean,
            ) {
                youtubePlayer = player

                player.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                    override fun onAdStarted() {}
                    override fun onLoading() {}
                    override fun onVideoStarted() { }
                    override fun onVideoEnded() {}
                    override fun onError(p0: YouTubePlayer.ErrorReason) {}
                    override fun onLoaded(videoId: String) { youtubePlayer!!.play() } })

            }
            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                result: YouTubeInitializationResult?,
            ) {
            }
        })
    }

    fun playVideo(youtubeId : String) {
        if (youtubePlayer != null) {
            if (youtubePlayer!!.isPlaying()) {
                youtubePlayer!!.pause()
            }
            youtubePlayer!!.cueVideo(youtubeId)
        }
    }
    
    fun showImage(name : String, message : String, showTime : String, type : Int, url : String) {
        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.Default).async {

            }.await()
            datas.apply {
                add(Data_chatting_basic(name = name, contents = message
                    , timedate = showTime, type = type, image = url))
            }

            adapter.datas = datas
            adapter.notifyDataSetChanged()
            binding.recycler.adapter = adapter

            binding.recycler.scrollToPosition(adapter.datas.size - 1)
        }
    }

    fun sendTime(time : String) : String {
        var date = Date(time.toLong())
        var formatTime = SimpleDateFormat("hh mm")
        var resultTime = formatTime.format(date)

        var roomHour = resultTime.split(" ").get(0).toInt().plus(9)
        var roomMinute = resultTime.split(" ").get(1)
        var showTime : String? = null
        if (roomHour >= 24) {
            showTime = roomHour.minus(24).toString()
        } else {
            showTime = roomHour.toString()
        }

        var returnTime = showTime+"시 " + roomMinute + "분"

        return returnTime
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
                    if (it.send_user_id == myId){
                        type = multi_type2
                    } else {
                        type = multi_type1
                    }

                    val url = Preferences.prefs.getString(it.send_user_id+"_url","null")
                    val name = Preferences.prefs.getString(it.send_user_id+"_name","null")
                    var showTime = sendTime(it.createdat.toString())

                    if (URLUtil.isValidUrl(it.message.toString())){
                        if (it.message.toString().contains("youtube.com/watch")) {

                        } else {
                            showImage(name, it.message.toString(), showTime, multi_type4, url)
                        }

                    } else {
                        datas.apply {
                            add(Data_chatting_basic(name = name, contents = it.message.toString()
                                , timedate = showTime, type = type!!, image = url))
                        }
                        adapter.datas = datas
                        adapter.notifyDataSetChanged()
                        binding.recycler.adapter = adapter

                        binding.recycler.scrollToPosition(adapter.datas.size - 1)
                    }
                }
            }

            override fun onFailure(call: Call<CallChats>, t: Throwable) {
                Log.d("TAG", t.toString())
            }
        })
    }

    fun selectGallery() {
        var intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        intent.type = "image/*"
        startActivityForResult(intent, REQ_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when(requestCode) {
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        selectUri = uri
                        if (!selectUri.toString().isNullOrEmpty()) {
                            uploadImage()
                        }
                    }
                }
            }
        }
    }

    fun getRealPath(uri: Uri) : String {
        var result : String? = null
        var cursor = contentResolver.query(uri,null,null,null,null)

        if(cursor == null) {
            result = uri.path
        } else {
            cursor.moveToFirst()
            var idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result.toString()
    }

    private fun uploadImage() {
        if (!selectUri.toString().equals("null")) {
            input = selectUri?.let { getRealPath(it) }
            val file = File(input)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val body = MultipartBody.Part.createFormData("profile-img",file.name,requestFile)

            server?.uploadImg(body)?.enqueue(object : Callback<UploadImg> {
                override fun onResponse(call: Call<UploadImg>, response: Response<UploadImg>) {
                    changeUrl = response.body()!!.data!!.asString.toString()

                    var messageObj = JSONObject()
                    messageObj.put("room_id", room_id)
                    messageObj.put("send_user_id", myId)
                    messageObj.put("message", changeUrl.toString())
                    messageObj.put("not_read", 1)

                    mSocket.emit("message", messageObj)
                }

                override fun onFailure(call: Call<UploadImg>, t: Throwable) {
                    Log.d("TAG", t.toString())
                }
            })
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA),
            REQUEST_IMAGE_CAPTURE)
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
        mSocket.disconnect()
    }
}