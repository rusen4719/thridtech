package org.techtown.thridtech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.techtown.thridtech.databinding.FragmentChatRoomBinding
import org.techtown.thridtech.databinding.FragmentFriendsBinding

// 바인딩 객체 타입에 ?를 붙여서 null을 허용 해줘야한다. ( onDestroy 될 때 완벽하게 제거를 하기위해 )
private var mBinding: FragmentFriendsBinding? = null
// 매번 null 체크를 할 필요 없이 편의성을 위해 바인딩 변수 재 선언
private val binding get() = mBinding!!

class friends : Fragment() {

    lateinit var adapter: Adapter_Friends
    val datas = mutableListOf<Data_Friends>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendsBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        adapter = Adapter_Friends(requireActivity())
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter

        datas.apply {
            add(Data_Friends(image = R.drawable.sample_1, name = "항래"))
            add(Data_Friends(image = R.drawable.sample_1, name = "안동현"))
            add(Data_Friends(image = R.drawable.sample_1, name = "이재룡"))
            add(Data_Friends(image = R.drawable.sample_1, name = "Tom"))
            add(Data_Friends(image = R.drawable.sample_1, name = "R.D.J"))
            add(Data_Friends(image = R.drawable.sample_1, name = "실리안"))
            add(Data_Friends(image = R.drawable.sample_1, name = "조승우"))
            add(Data_Friends(image = R.drawable.sample_1, name = "Cristin"))
            add(Data_Friends(image = R.drawable.sample_1, name = "드웨인 존슨"))
            add(Data_Friends(image = R.drawable.sample_1, name = "스칼렛 요한슨"))
            add(Data_Friends(image = R.drawable.sample_1, name = "안예은"))
            add(Data_Friends(image = R.drawable.sample_1, name = "Cris"))
        }
        adapter.datas = datas
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroyView()
    }

}