package org.techtown.thridtech

data class Data_Chat_Room(
    val title : String,
    val last_msg : String,
    val receive_time : String,
    val participant : ArrayList<String>,
    val room_id : String,
    val lastChatDate : String
)
