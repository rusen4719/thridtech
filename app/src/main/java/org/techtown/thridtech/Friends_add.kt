package org.techtown.thridtech

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import org.techtown.thridtech.databinding.ActivityCreateAccountBinding
import org.techtown.thridtech.databinding.ActivityFriendsAddBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private var mBinding: ActivityFriendsAddBinding? = null
private val binding get() = mBinding!!

class Friends_add : AppCompatActivity() {

    val url = "https://chatdemo2121.herokuapp.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Thridtech)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_friends_add)
        mBinding = ActivityFriendsAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        var layoutInflater = LayoutInflater.from(applicationContext).inflate(R.layout.view_holder_toast,null)
        var text : TextView = layoutInflater.findViewById(R.id.textViewToast)

        binding.btnFrdSearch.setOnClickListener {
            val frdSearchName = binding.edtFrdId.text.toString()

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var server = retrofit?.create(APIInterface::class.java)

            var jsonInfo = JsonObject()
            jsonInfo.addProperty("name", frdSearchName)

            server?.searchFriends(jsonInfo)?.enqueue(object : Callback<SearchFriends> {
                override fun onResponse(call: Call<SearchFriends>, response: Response<SearchFriends>) {

                    if (response.body()?.data?.asJsonArray.toString().length > 10) {
                        binding.frdAddMainLayout.visibility = View.VISIBLE
                        response.body()?.data?.asJsonArray!!.forEach {
                            val name = it.asJsonObject.get("name").asString
                            val id = it.asJsonObject.get("user_id").asString
                            val profile = it.asJsonObject.get("profile_img_url").asString

                            Log.d("TAG", "profile " + profile)

                            Preferences.prefs.setString("add_frd_name", name)
                            Preferences.prefs.setString("add_frd_id", id)

                            Glide.with(applicationContext).load(profile).into(binding.image)
                            binding.frdAddName.text = name

                        }
                    } else {
                        binding.frdAddMainLayout.visibility = View.INVISIBLE
                        text.text = "존재하지 않는 이름입니다."

                        var toast = Toast(applicationContext)
                        toast.view = layoutInflater
                        toast.setGravity(0, 0, 0)
                        toast.show()
                    }

                }

                override fun onFailure(call: Call<SearchFriends>, t: Throwable) {
                    Log.d("TAG", t.toString())
                }
            })
        }

        binding.btnFrdAdd.setOnClickListener {
            val frdSearchName = binding.edtFrdId.text.toString()
            val frdName = Preferences.prefs.getString("add_frd_name", "no name")
            val frdId = Preferences.prefs.getString("add_frd_id", "no id")
            val myId = Preferences.prefs.getString("MyID", "no myId")

            var bool : Boolean? = null

            var jsonAddFirends = JsonObject()
            jsonAddFirends.addProperty("my_id", myId)
            jsonAddFirends.addProperty("friend_id", frdId)
            jsonAddFirends.addProperty("friend_name", frdName)

            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var server = retrofit?.create(APIInterface::class.java)

            var jsonFindFriends = JsonObject()
            jsonFindFriends.addProperty("id", myId)

            server?.findFriends(myId)?.enqueue(object : Callback<FindFriend> {
                override fun onResponse(call: Call<FindFriend>, response: Response<FindFriend>) {

                    response.body()?.data?.asJsonArray?.forEach {

                        val name = it.asJsonObject.get("name").asString
                        if (name == frdSearchName){
                            bool = true
                        }
                    }
                    if (bool == true) {
                        text.text = "이미 있는 친구입니다."

                        var toast = Toast(applicationContext)
                        toast.view = layoutInflater
                        toast.setGravity(0, 0, 0)
                        toast.show()
                    } else {

                        server?.addFriends(jsonAddFirends)?.enqueue(object : Callback<AddFriends> {
                            override fun onResponse(call: Call<AddFriends>, response: Response<AddFriends>) {
                                Log.d("TAG", response.body()?.data.toString())
                                Log.d("TAG", response.body()?.msg.toString())
                                val newCheck = Preferences.prefs.getString("frdList_renewal", "no value")
                                Preferences.prefs.setString("frdList_renewal", (newCheck.toInt()+1).toString())

                                text.text = "친구추가 되었습니다."

                                var toast = Toast(applicationContext)
                                toast.view = layoutInflater
                                toast.setGravity(0, 0, 0)
                                toast.show()
                            }

                            override fun onFailure(call: Call<AddFriends>, t: Throwable) {
                                Log.d("TAG", t.toString())
                            }
                        })
                    }
                }

                override fun onFailure(call: Call<FindFriend>, t: Throwable) {
                    Log.d("TAG", "Fail to Find Friends")
                }
            })

        }
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }
}