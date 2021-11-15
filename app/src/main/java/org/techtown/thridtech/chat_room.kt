package org.techtown.thridtech

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import org.techtown.thridtech.databinding.FragmentChatRoomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

private var mBinding: FragmentChatRoomBinding? = null
    private val binding get() = mBinding!!

class chat_room : Fragment() {

    val url = "https://chatdemo2121.herokuapp.com/"

    lateinit var adapter: Adapter_Chat_Room
    val datas = mutableListOf<Data_Chat_Room>()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var server = retrofit?.create(APIInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().setTheme(R.style.Theme_Thridtech)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentChatRoomBinding.inflate(inflater, container, false)

        adapter = Adapter_Chat_Room(requireActivity())
        binding.recycler.setHasFixedSize(true)

        initRecycler()

        binding.swipeRefresh.setOnRefreshListener {
            initRecycler()
            binding.swipeRefresh.isRefreshing = false
        }

        binding.makeChatRoom.setOnClickListener {
            val intent = Intent(context, MakeChatRoom::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun initRecycler() {
        datas.clear()
        val myId = Preferences.prefs.getString("MyID", "no myId")

        server?.findChatRooms(myId)?.enqueue(object : Callback<FindChatRooms> {
            @SuppressLint("NewApi")
            override fun onResponse(call: Call<FindChatRooms>, response: Response<FindChatRooms>) {

                response.body()?.data?.asJsonArray?.forEach {

                    var showDate : String? = null

                    val roomId = it.asJsonObject.get("room_id").asString
                    val identify = it.asJsonObject.get("identifier").asString
                    val roomType = it.asJsonObject.get("room_type").asString
                    val lastChat = it.asJsonObject.get("last_chat").asString
                    var updateDate = it.asJsonObject.get("updatedat").asString

                    var date = Date(updateDate.toLong() )
                    var formatDate = SimpleDateFormat("yyyy MM dd")
                    var formatTime = SimpleDateFormat("HH mm")
                    var resultDate = formatDate.format(date)
                    var resultTime = formatTime.format(date)

                    var roomYear = resultDate.split(" ").get(0)
                    var roomMonth = resultDate.split(" ").get(1)
                    var roomDay = resultDate.split(" ").get(2)
                    var roomHour = resultTime.split(" ").get(0).toInt()
                    var showTime : String? = null
                    if (roomHour >= 24) {
                        showTime = roomHour.minus(24).toString()
                    } else {
                        showTime = roomHour.toString()
                    }

                    var roomMinute = resultTime.split(" ").get(1)

                    var currentDate = Date(System.currentTimeMillis())
                    var formatCurrnetDate = SimpleDateFormat("yyyy MM dd")
                    var formatCurrentTime = SimpleDateFormat("hh mm")
                    var resultCurrentDate = formatCurrnetDate.format(currentDate)
                    var resultCurrentTime = formatCurrentTime.format(currentDate)

                    var currentYear = resultCurrentDate.split(" ").get(0)
                    var currentMonth = resultCurrentDate.split(" ").get(1)
                    var currentDay = resultCurrentDate.split(" ").get(2)

                    if (roomYear == currentYear && roomMonth == currentMonth && roomDay == currentDay) {
                        showDate = showTime + "시 " + roomMinute + "분"
                    } else if (roomYear == currentYear && roomMonth == currentMonth || roomDay != currentDay) {
                        showDate = roomMonth + "월 " + roomDay + "일"
                    } else if (roomYear != currentYear) {
                        showDate = roomYear + "년 " + roomMonth + "월 " + roomDay + "일"
                    }

                    if (updateDate == "") {
                        showDate = ""
                    }

                    val roomName = it.asJsonObject.get("room_name").asString
                    val notReadChat = it.asJsonObject.get("not_read_chat").asString
                    val lastReadChatId = it.asJsonObject.get("last_read_chat_id").asString
                    val participant3 = it.asJsonObject.get("participant").asJsonArray
                    val partiSize = participant3.size()
                    var partiArray = mutableListOf<String>()

                    for (i : Int in 0..partiSize-1) {
                        partiArray.add(participant3.get(i).asString)

                    }

                    datas.apply {
                        add(Data_Chat_Room( title = roomName,
                            last_msg = lastChat, receive_time = showDate.toString(),
                            participant = partiArray as ArrayList<String>, room_id = roomId,
                            lastChatDate = updateDate))

                    }
                    
                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<FindChatRooms>, t: Throwable) {
                Log.d("TAG", t.toString())
            }
        })

        binding.recycler.adapter = adapter
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
        }
    }

