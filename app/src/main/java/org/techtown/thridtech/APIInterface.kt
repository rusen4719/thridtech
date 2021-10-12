package org.techtown.thridtech

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

data class CreateID(@SerializedName("msg") val msg : String)

data class Login(
    @SerializedName("data") var data: JSONObject? = null)

data class ResponseDC(@SerializedName("msg") val msg : String? = null)
data class OverlapID(@SerializedName("msg") val msg : String? = null)

interface APIInterface {
    @GET("/api/user/{id}")
    fun getRequest(@Query("user_id") user_id: String): Call<OverlapID>

    @POST("/api/auth/signup")
    fun postRequest(@Body jsonObj: JsonObject):Call<CreateID>

    @FormUrlEncoded
    @POST("/api/auth/login")
    fun login(@Field("user_id")user_id: String,
              @Field("password")password: String): Call<Login>

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>
}