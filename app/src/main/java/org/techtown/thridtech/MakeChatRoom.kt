package org.techtown.thridtech

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.techtown.thridtech.databinding.ActivityFriendsAddBinding
import org.techtown.thridtech.databinding.ActivityMakeChatRoomBinding
import org.techtown.thridtech.databinding.FragmentFriendsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private var mBinding: ActivityMakeChatRoomBinding? = null
private val binding get() = mBinding!!

class MakeChatRoom : AppCompatActivity() {

    lateinit var adapter: Adapter_Make_CharRoom
    val datas = mutableListOf<Data_Make_ChatRoom>()

    val url = "https://chatdemo2121.herokuapp.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var server = retrofit?.create(APIInterface::class.java)

    var myId = Preferences.prefs.getString("MyID", null.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_make_chat_room)
        mBinding = ActivityMakeChatRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var chatRoomName = binding.edtChatRoomName

        var layoutInflater = LayoutInflater.from(applicationContext).inflate(R.layout.view_holder_toast,null)
        var text : TextView = layoutInflater.findViewById(R.id.textViewToast)

        init()

        binding.btnBack.setOnClickListener { finish() }

        binding.makeChatRoomConfirm.setOnClickListener {
            if (chatRoomName.text.isNullOrEmpty()) {
                text.text="채팅방 이름을 입력해주세요."

                var toast = Toast(this)
                toast.view = layoutInflater
                toast.setGravity(0,0,0)
                toast.show()
            } else {
                var identify = myId + "-"
                var partyJsonArray = JsonArray()

                var jsonObj = JsonObject()

                datas.forEach {
                    if (it.checked == true) {
                        var jsonParty = JsonObject()
                        identify = identify + it.id + "-"
                        jsonParty.addProperty("user_id",it.id)
                        partyJsonArray.add(jsonParty)
                    }
                }

                var strName = chatRoomName.text.toString()
                var subStrName = strName.substring(1,strName.length-1)

                var strIden = identify.substring(0,identify.length-1)

                jsonObj.addProperty("my_id",myId)
                jsonObj.addProperty("room_type","group")
                jsonObj.addProperty("room_name", chatRoomName.text.toString())
                jsonObj.addProperty("identifier",strIden)
                jsonObj.add("participant",partyJsonArray)

                Log.d("TAG", "strName : " + strName)

                server?.makeChatRoom(jsonObj)?.enqueue(object : Callback<MakeChatRooms> {
                    override fun onResponse(call: Call<MakeChatRooms>, response: Response<MakeChatRooms>) {
                        Log.d("TAG", response.body()?.data.toString())
                        Log.d("TAG", response.body()?.msg.toString())
                        Log.d("TAG", "연결됨")
                    }

                    override fun onFailure(call: Call<MakeChatRooms>, t: Throwable) {
                        Log.d("TAG", t.toString())
                    }
                })
            }
        }
    }

    fun init() {

        depolyFriends()
    }

    fun depolyFriends() {
        datas.clear()

        var jsonInfo = JsonObject()
        jsonInfo.addProperty("id", myId)

        adapter = Adapter_Make_CharRoom(applicationContext)
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter

        server?.findFriends(myId)?.enqueue(object : Callback<FindFriend> {
            override fun onResponse(call: Call<FindFriend>, response: Response<FindFriend>) {
                response.body()?.data?.asJsonArray?.forEach {

                    val name = it.asJsonObject.get("name").asString
                    val id = it.asJsonObject.get("user_id").asString
                    val profile = it.asJsonObject.get("profile_img_url").asString
                    val status = it.asJsonObject.get("status_msg").asString

                    datas.apply {

                        if(profile.isNullOrEmpty()) {
                            add(Data_Make_ChatRoom(image = "", name = name,checked = false, id = id))
                        } else {
                            add(Data_Make_ChatRoom(image = profile, name = name, checked = false, id = id))
                        }
                    }
                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                Log.d("TAG", "Fail to Find Friends")
            }
        })
    }

    fun enabeldBtn(count : Int) {
        val chatConfirm = binding.makeChatRoomConfirm

        if (count > 0) {
            chatConfirm.isEnabled = true
        } else if (count <= 0) {
            chatConfirm.isEnabled = false
        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}