package org.techtown.thridtech

import android.Manifest
import android.Manifest.permission.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.techtown.thridtech.databinding.ActivityChangeMyProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import kotlin.reflect.typeOf

private var mBinding: ActivityChangeMyProfileBinding? = null
private val binding get() = mBinding!!

class ChangeMyProfile : AppCompatActivity() {
    val url = "https://chatdemo2121.herokuapp.com/"

    var myId = Preferences.prefs.getString("MyID", "null")
    var myObjectId = Preferences.prefs.getString("MyObjectId", null.toString())
    val myName = Preferences.prefs.getString(myId+"_name", null.toString())
    val myStatus = Preferences.prefs.getString("MyStatus", null.toString())
    val myUrl = Preferences.prefs.getString(myId+"_url", null.toString())

    var changeUrl :String? = null

    val REQ_GALLERY = 12
    var myUri : Uri? = null

    val REQUEST_IMAGE_CAPTURE = 1

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var server = retrofit?.create(APIInterface::class.java)

    var input : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Thridtech)
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_change_my_profile2)
        mBinding = ActivityChangeMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()

        binding.scroll.setOnClickListener { hideKeyboard() }
        binding.changeProfileMainlayout.setOnClickListener { hideKeyboard() }
        binding.btnBack.setOnClickListener { finish() }

        binding.changeProfileName.setText(myName)
        binding.changeProfileStatus.setText(myStatus)

        Glide.with(applicationContext).load(myUrl).into(binding.image)

        binding.saveChangeProfile.setOnClickListener {
            var permission = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)

            if (myUri.toString().equals("null")) {
                changeProfile()
            } else {
                uploadImage()
            }
        }

        binding.btnChangeProfile.setOnClickListener {
            selectGallery()
        }
    }

    private fun uploadImage() {
        if (!myUri.toString().equals("null")) {
            input = myUri?.let { getRealPath(it) }
            val file = File(input)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val body = MultipartBody.Part.createFormData("profile-img",file.name,requestFile)

            server?.uploadImg(body)?.enqueue(object : Callback<UploadImg> {
                override fun onResponse(call: Call<UploadImg>, response: Response<UploadImg>) {
                    changeUrl = response.body()!!.data!!.asString.toString()
                    Log.d("TAG", response.body()!!.msg.toString())
                    changeProfile()
                }

                override fun onFailure(call: Call<UploadImg>, t: Throwable) {
                    Log.d("TAG", t.toString())
                }
            })
        }
    }

    private fun changeProfile() {
        var jsonInfo = JsonObject()
        jsonInfo.addProperty("id", myObjectId)
        jsonInfo.addProperty("name", binding.changeProfileName.text.toString())
        jsonInfo.addProperty("status_msg", binding.changeProfileStatus.text.toString())
        if (!changeUrl.isNullOrEmpty()) {
            jsonInfo.addProperty("profile_img_url", changeUrl)
        } else {
            val url = Preferences.prefs.getString(myId+"_url","no")
            jsonInfo.addProperty("profile_img_url", url)
        }

        server?.changeProfile(jsonInfo)?.enqueue(object : Callback<ChangeProfile> {
            override fun onResponse(call: Call<ChangeProfile>, response: Response<ChangeProfile>) {
                Preferences.prefs.setString(myId+"_name", binding.changeProfileName.text.toString())
                Preferences.prefs.setString("MyStatus", binding.changeProfileStatus.text.toString())
                if (!changeUrl.isNullOrEmpty()) {

                    Preferences.prefs.setString(myId+"_url", changeUrl.toString())
                }

                finish()
            }

            override fun onFailure(call: Call<ChangeProfile>, t: Throwable) {
                Log.d("TAG", "실패")
                Log.d("TAG", t.toString())
            }
        })
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            REQUEST_IMAGE_CAPTURE)
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
                        myUri = uri
                        binding.image.setImageURI(uri)
                    }
                }
            }
        }
    }

    fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.changeProfileName.windowToken, 0)
        binding.changeProfileName.clearFocus()
        binding.changeProfileStatus.clearFocus()
    }

    override fun onDestroy() {
        mBinding = null
        super.onDestroy()
    }

}