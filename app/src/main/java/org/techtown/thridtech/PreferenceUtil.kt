package org.techtown.thridtech

import android.content.Context

class PreferenceUtil(context: Context) {
    private val prefs = context.getSharedPreferences("prefs_name",Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key,defValue).toString()
    }

    fun setString(key: String, str: String) {
        prefs.edit().putString(key,str).apply()
    }

/*  데이터 조회
    MyApplication.prefs.getString("email", "no email")
    데이터 저장
    MyApplication.prefs.setString("email", "abcd@gmail.com")*/
}