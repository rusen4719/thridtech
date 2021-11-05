package org.techtown.thridtech

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter_Make_CharRoom(private val context: Context) : RecyclerView.Adapter<Adapter_Make_CharRoom.ViewHolder>() {
    var datas = mutableListOf<Data_Make_ChatRoom>()

    var checkedCount = 0

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val image = itemView.findViewById<ImageView>(R.id.make_chatRoom_img)
        private val name = itemView.findViewById<TextView>(R.id.make_chatRoom_name)
        private val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)

        fun bind(item: Data_Make_ChatRoom) {
            val chatObject = MakeChatRoom()

            name.text = item.name
            if (item.image.isNullOrEmpty()) {
                Glide.with(itemView).load(R.drawable.sample_1).into(image)
            } else {
                Glide.with(itemView).load(item.image).into(image)
            }

            if (checkBox.isChecked == true && item.checked == true) {
                checkBox.isChecked = true
            } else if (checkBox.isChecked == true && item.checked == false) {
                checkBox.isChecked = false
            } else if(checkBox.isChecked == false && item.checked == true) {
                checkBox.isChecked = true
            }

            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    item.checked = true
                    checkedCount++
                } else {
                    item.checked = false
                    checkedCount--
                }

                chatObject.enabeldBtn(checkedCount)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_make_chatroom,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int = datas.size
}