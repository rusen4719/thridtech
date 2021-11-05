package org.techtown.thridtech

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.lang.reflect.Array
import com.google.gson.annotations.Expose




data class CreateID(
    @SerializedName("data") var data: Boolean? = null,
    @SerializedName("msg") val msg: String,
)

data class Login(
    @SerializedName("data") var data: JsonObject? = null,
    @SerializedName("msg") var msg: String? = null,
)

data class ResponseDC(@SerializedName("msg") val msg : String? = null)
data class OverlapID(
    @SerializedName("data") val data: Boolean? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class FindFriend(
    @SerializedName("data") val data: JsonArray? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class FindChatRooms(
    @SerializedName("data") val data: JsonArray? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class MakeChatRooms(
    @SerializedName("data") val data: JsonObject? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class CallChats(
    @SerializedName("data") val data: ArrayList<GetList>,
    @SerializedName("msg") val msg: String? = null,
)

data class SearchFriends(
    @SerializedName("data") val data: JsonArray? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class AddFriends(
    @SerializedName("data") val data: JsonPrimitive? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class ChangeProfile(
    @SerializedName("data") val data: Boolean? = null,
    @SerializedName("msg") val msg: String? = null,
)

data class UploadImg(
    @SerializedName("data") val data: JsonPrimitive? = null,
    @SerializedName("msg") val msg: String? = null,
)

interface APIInterface {
    @GET("/api/friend/{id}")
    fun findFriends(@Path ("id")id : String): Call<FindFriend>

    @GET("/api/chat/roomList/{user_id}")
    fun findChatRooms(@Path ("user_id")id : String): Call<FindChatRooms>

    @GET("/api/chat/room")
    fun callChats(@Query ("room_id")id : String,
                  @Query ("cursor")cursor : String): Call<CallChats>

    @POST("/api/chat/room/create")
    fun makeChatRoom(@Body jsonObj: JsonObject): Call<MakeChatRooms>

    @POST("/api/auth/signup")
    fun createAccount(@Body jsonObj: JsonObject):Call<CreateID>

    @POST("/api/user/search/user")
    fun searchFriends(@Body jsonObj: JsonObject): Call<SearchFriends>

    @POST("/api/friend/add")
    fun addFriends(@Body jsonObj: JsonObject): Call<AddFriends>

    @POST("/api/auth/login")
    fun login(@Body jsonObj: JsonObject): Call<Login>

    @POST("/api/user/check/validation")
    fun overlap(@Body jsonObj: JsonObject): Call<OverlapID>

    @Multipart
    @POST("/api/user/upload")
    fun uploadImg(@Part url: MultipartBody.Part): Call<UploadImg>

    @POST("/api/user/profile/change")
    fun changeProfile(@Body jsonObj: JsonObject): Call<ChangeProfile>

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(
        @Path("id") id: String,
        @Field("content") content: String,
    ): Call<ResponseDC>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseDC>
}
class GetList {
    @SerializedName("_id")
    @Expose
    private val id : String? = null

    @SerializedName("room_id")
    @Expose
    val room_id: String? = null

    @SerializedName("send_user_id")
    @Expose
    val send_user_id: String? = null

    @SerializedName("message")
    @Expose
    val message: String? = null

    @SerializedName("createdat")
    @Expose
    val createdat: String? = null

    fun getId(): String {
        return id.toString()
    }

}