package org.techtown.thridtech

import android.content.Intent
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

    var check_frdNums  = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentFriendsBinding.inflate(inflater, container, false)

        initRecycler()

        binding.myProfile.setOnClickListener {
            val intent = Intent(context, ShowMyProfile::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    fun initRecycler() {
        datas.clear()
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
                val array : ArrayList<String>? = null

                response.body()?.data?.asJsonArray?.forEach {

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

                check_frdNums = datas.size
                Preferences.prefs.setString("frdList_renewal", check_frdNums.toString())
                Preferences.prefs.setString("frdList_renewal_prepare", check_frdNums.toString())
            }

            override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                Log.d("TAG", "Fail to Find Friends")
            }
        })

        binding.frdAdd.setOnClickListener{
            val intent = Intent(context, Friends_add::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        val newCheck = Preferences.prefs.getString("frdList_renewal", "no value")
        val check = Preferences.prefs.getString("frdList_renewal_prepare", "no value")

        Log.d("TAG", check.toString())
        Log.d("TAG", check_frdNums.toString())
        Log.d("TAG", check_frdNums.toString())

        if (check == "no value" || newCheck == "no value") {

        } else if(check.toInt() < newCheck.toInt()) {
            initRecycler()
            check_frdNums = 0
        }
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}