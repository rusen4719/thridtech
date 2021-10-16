package org.techtown.thridtech

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import java.lang.reflect.Array

data class CreateID( @SerializedName("data") var data: Boolean? = null,
                     @SerializedName("msg") val msg : String)

data class Login(@SerializedName("data") var data: JsonObject? = null,
                 @SerializedName("msg") var msg: String? = null)

data class ResponseDC(@SerializedName("msg") val msg : String? = null)
data class OverlapID(@SerializedName("data") val data : Boolean? = null,
                     @SerializedName("msg") val msg : String? = null)

data class FindFriend(@SerializedName("data") val data : JsonArray? = null,
                     @SerializedName("msg") val msg : String? = null)

interface APIInterface {
    @GET("/api/friend/{id}")
    fun findFriends(@Path ("id")id : String): Call<FindFriend>

    @GET("/api/friend/{id}")
    fun findFriends1(@Body jsonObj: JsonObject): Call<FindFriend>

    @POST("/api/auth/signup")
    fun createAccount(@Body jsonObj: JsonObject):Call<CreateID>

    @FormUrlEncoded
    @POST("/api/auth/login")
    fun logi1n(@Field("user_id")user_id: String,
              @Field("password")password: String): Call<Login>


    @POST("/api/auth/login")
    fun login(@Body jsonObj: JsonObject): Call<Login>

    @POST("/api/user/check/validation")
    fun overlap(@Body jsonObj: JsonObject): Call<OverlapID>

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>
}