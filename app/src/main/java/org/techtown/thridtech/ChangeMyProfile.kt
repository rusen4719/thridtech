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
import com.google.gson.JsonObject
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
import kotlin.reflect.typeOf

private var mBinding: ActivityChangeMyProfileBinding? = null
private val binding get() = mBinding!!

class ChangeMyProfile : AppCompatActivity() {
    val url = "https://chatdemo2121.herokuapp.com/"

    var myId = Preferences.prefs.getString("MyID", null.toString())
    val myName = Preferences.prefs.getString("MyName", null.toString())
    val myStatus = Preferences.prefs.getString("MyStatus", null.toString())

    val REQ_GALLERY = 12
    var uri : Uri? = null

    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_change_my_profile2)
        mBinding = ActivityChangeMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()

        binding.scroll.setOnClickListener { hideKeyboard() }
        binding.changeProfileMainlayout.setOnClickListener { hideKeyboard() }
        binding.btnBack.setOnClickListener { finish() }

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var server = retrofit?.create(APIInterface::class.java)

        binding.changeProfileName.setText(myName)
        binding.changeProfileStatus.setText(myStatus)

        var file = File(uri.toString())

        binding.saveChangeProfile.setOnClickListener {
            val input = getRealPath(uri!!)
            var permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            val file = File(input)
            val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
            val body = MultipartBody.Part.createFormData("profile-img",file.name,requestFile)

            server?.uploadImg1(body)?.enqueue(object : Callback<UploadImg> {
                override fun onResponse(call: Call<UploadImg>, response: Response<UploadImg>) {
                    Log.d("TAG", response.body()?.msg.toString())
                    Log.d("TAG", response.body()?.data.toString())
                }

                override fun onFailure(call: Call<UploadImg>, t: Throwable) {
                    Log.d("TAG", "실패")
                    Log.d("TAG", t.toString())
                }
            })
        }

        binding.btnChangeProfile.setOnClickListener {
            selectGallery()
        }
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
                        this.uri = uri
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