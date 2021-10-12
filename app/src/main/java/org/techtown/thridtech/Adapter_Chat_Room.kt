package org.techtown.thridtech

import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter_Chat_Room(private val context: Context) : RecyclerView.Adapter<Adapter_Chat_Room.ViewHolder>() {
    var datas = mutableListOf<Data_Chat_Room>()

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val title = itemView.findViewById<TextView>(R.id.title)
        private val last_msg = itemView.findViewById<TextView>(R.id.last_msg)
        private val receive_time = itemView.findViewById<TextView>(R.id.receive_time)

        fun bind(item: Data_Chat_Room) {
            title.text = item.title
            last_msg.text = item.last_msg
            receive_time.text = item.receive_time
            Glide.with(itemView).load(item.image).into(image)
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
