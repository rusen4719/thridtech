package org.techtown.thridtech

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.socket.client.IO
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

class Adapter_Friends(private val context: Context) : RecyclerView.Adapter<Adapter_Friends.ViewHolder>() {
    var datas = mutableListOf<Data_Friends>()

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.frd_name)
        private val status = itemView.findViewById<TextView>(R.id.frd_status)

        fun bind(item: Data_Friends) {
            name.text = item.name
            status.text = item.status
            if (item.image.isNullOrEmpty()) {
                Glide.with(itemView).load(R.drawable.sample_1).into(image)
            } else {
                Glide.with(itemView).load(item.image).into(image)
            }

            itemView.setOnClickListener {
                val intent = Intent(context, Chatting::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_friends,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size
}
