package org.techtown.thridtech

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.techtown.thridtech.databinding.FragmentChatRoomBinding
import java.lang.Exception

// 바인딩 객체 타입에 ?를 붙여서 null을 허용 해줘야한다. ( onDestroy 될 때 완벽하게 제거를 하기위해 )
    private var mBinding: FragmentChatRoomBinding? = null
// 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
    private val binding get() = mBinding!!

class chat_room : Fragment() {

    lateinit var adapter: Adapter_Chat_Room
    val datas = mutableListOf<Data_Chat_Room>()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentChatRoomBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        adapter = Adapter_Chat_Room(requireActivity())
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter

        datas.apply {
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
            add(Data_Chat_Room(image = R.drawable.sample_1, title = "채팅방 이름", last_msg = "마지막 채팅", receive_time = "11:45 am"))
        }
        adapter.datas = datas
        adapter.notifyDataSetChanged()
    }

    companion object {}

    // 프래그먼트가 destroy (파괴) 될때..
    override fun onDestroyView() {
    // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroyView()
        }
    }
