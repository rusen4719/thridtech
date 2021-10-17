package org.techtown.thridtech

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.techtown.thridtech.databinding.FragmentChatRoomBinding
import org.techtown.thridtech.databinding.FragmentFriendsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private var mBinding: FragmentFriendsBinding? = null
private val binding get() = mBinding!!

class friends : Fragment() {

    lateinit var adapter: Adapter_Friends
    val datas = mutableListOf<Data_Friends>()

    val url = "https://chatdemo2121.herokuapp.com/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendsBinding.inflate(inflater, container, false)

        initRecycler()

        return binding.root
    }

    private fun initRecycler() {
        var myId = Preferences.prefs.getString("MyID", null.toString())
        val myName = Preferences.prefs.getString("MyName", null.toString())
        val myStatus = Preferences.prefs.getString("MyStatus", null.toString())

        binding.myName.text = myName
        binding.myStatus.text = myStatus

        adapter = Adapter_Friends(requireActivity())
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var server = retrofit?.create(APIInterface::class.java)

        var jsonInfo = JsonObject()
        jsonInfo.addProperty("id", myId)

        Log.d("TAG", myId)

        server?.findFriends(myId)?.enqueue(object : Callback<FindFriend> {
            override fun onResponse(call: Call<FindFriend>, response: Response<FindFriend>) {

                val array = response.body()?.data?.asJsonArray?.forEach {

                    val name = it.asJsonObject.get("name").asString
                    val id = it.asJsonObject.get("user_id").asString
                    val profile = it.asJsonObject.get("profile_img_url").asString
                    val status = it.asJsonObject.get("status_msg").asString

                    datas.apply {
                        add(Data_Friends(image = R.drawable.sample_1, name = name, status = status))
                    }

                    adapter.datas = datas
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                Log.d("TAG", "Fail to Find Friends")
            }
        })

/*        datas.apply {
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
        }*/

    }

    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mBinding = null
        super.onDestroyView()
    }

}