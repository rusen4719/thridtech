package org.techtown.thridtech

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs = context.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)
    private val MY_ACCOUNT : String = "account"

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key,defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key,str).apply()
    }

    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }

    fun clearUser(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.commit()
    }


/*  데이터 조회
    MyApplication.prefs.getString("email", "no email")
    데이터 저장
    MyApplication.prefs.setString("email", "abcd@gmail.com")*/
}