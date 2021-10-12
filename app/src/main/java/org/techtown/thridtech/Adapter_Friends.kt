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

class Adapter_Friends(private val context: Context) : RecyclerView.Adapter<Adapter_Friends.ViewHolder>() {
    var datas = mutableListOf<Data_Friends>()

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.frd_name)

        fun bind(item: Data_Friends) {
            name.text = item.name
            Glide.with(itemView).load(item.image).into(image)
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
