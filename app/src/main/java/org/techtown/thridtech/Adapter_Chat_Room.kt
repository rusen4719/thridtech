package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class Adapter_Chat_Room(private val context: Context) : RecyclerView.Adapter<Adapter_Chat_Room.ViewHolder>() {
    var datas = mutableListOf<Data_Chat_Room>()

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        lateinit var mSocket: Socket

        private val title = itemView.findViewById<TextView>(R.id.title)
        private val last_msg = itemView.findViewById<TextView>(R.id.last_msg)
        private val receive_time = itemView.findViewById<TextView>(R.id.receive_time)
        
        fun bind(item: Data_Chat_Room) {
            try {
                mSocket = IO.socket("https://chatdemo2121.herokuapp.com/")
                mSocket.connect()
            } catch (e : URISyntaxException) { }

            mSocket.on(Socket.EVENT_CONNECT, onConnect)
            mSocket.on("message", onMessage)

            title.text = item.title
            last_msg.text = item.last_msg
            receive_time.text = item.receive_time
            //Glide.with(itemView).load(item.image).into(image)

            itemView.setOnClickListener {
                mSocket.emit("joinRoom", item.room_id)
                val intent = Intent(context, Chatting::class.java)
                intent.putExtra("title",item.title)
                intent.putExtra("parti",item.participant)
                intent.putExtra("room_id",item.room_id)
                context.startActivity(intent)
            }
        }

        val onConnect = Emitter.Listener {
            Log.d("TAG", "onConnect : " + it.toString())
        }

        val onMessage = Emitter.Listener {
            Log.d("TAG", "onMessage : " + it.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat_room,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    override fun getItemCount(): Int = datas.size

}
