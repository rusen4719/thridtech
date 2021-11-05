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

class Adapter_Chatting(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var datas = mutableListOf<Data_chatting_basic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder {
        val view : View?
        return when(viewType) {
            multi_type1 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chatting_basic,
                    parent,
                    false
                )
                MultiViewHolder1(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_chatting_mine,
                    parent,
                    false
                )
                MultiViewHolder2(view)
            }
        }
    }
    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int {
        return datas[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(datas[position].type) {
            multi_type1 -> {
                (holder as MultiViewHolder1).bind(datas[position])
                holder.setIsRecyclable(false)
            }
            multi_type2 -> {
                (holder as MultiViewHolder2).bind(datas[position])
                holder.setIsRecyclable(false)
            }
        }
    }

    inner class MultiViewHolder1(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.user_name)
        private val contents: TextView = view.findViewById(R.id.contents)
        private val timeDate: TextView = view.findViewById(R.id.time_date)
        private val image: ImageView = view.findViewById(R.id.image)

        fun bind(item: Data_chatting_basic) {
            name.text = item.name
            contents.text = item.contents
            timeDate.text = item.timedate
            Glide.with(itemView).load(item.image).into(image)
        }
    }
    inner class MultiViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

        private val name: TextView = view.findViewById(R.id.user_name)
        private val contents: TextView = view.findViewById(R.id.contents)
        private val timeDate: TextView = view.findViewById(R.id.time_date)
        private val image: ImageView = view.findViewById(R.id.image)

        fun bind(item: Data_chatting_basic) {
            name.text = item.name
            contents.text = item.contents
            timeDate.text = item.timedate
            Glide.with(itemView).load(item.image).into(image)
        }
    }

}
