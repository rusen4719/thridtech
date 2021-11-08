package org.techtown.thridtech

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.techtown.thridtech.databinding.FragmentChatRoomBinding
import org.techtown.thridtech.databinding.FragmentFriendsBinding
import org.techtown.thridtech.databinding.FragmentSettingsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private var mBinding: FragmentSettingsBinding? = null
private val binding get() = mBinding!!

class settings : Fragment() {

    lateinit var adapter: Adapter_Settings
    val datas = mutableListOf<Data_settings>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        adapter = Adapter_Settings(requireActivity())
        binding.recycler.setHasFixedSize(true)

        initRecycler()

        return binding.root
    }

    fun initRecycler() {
       datas.apply {
           add(Data_settings(text = "로그아웃", icon = R.drawable.ic_baseline_highlight_off_24))
       }
        adapter.datas = datas
        adapter.notifyDataSetChanged()
        binding.recycler.adapter = adapter
    }


    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

}