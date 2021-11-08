package org.techtown.thridtech

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Context.MODE_PRIVATE

class Adapter_Settings(private val context: Context) : RecyclerView.Adapter<Adapter_Settings.ViewHolder>() {
    var datas = mutableListOf<Data_settings>()

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        private val text = itemView.findViewById<TextView>(R.id.setting_txt)
        private val icon = itemView.findViewById<ImageView>(R.id.setting_icon)

        fun bind(item: Data_settings) {
            text.text = item.text
            icon.setImageResource(item.icon)

            if (item.text == "로그아웃") {
                itemView.setOnClickListener {
                    Preferences.prefs.clearUser(context)
                    val intent = Intent(context, LoginActivity::class.java)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_settings,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])

    }

    override fun getItemCount(): Int = datas.size

}
