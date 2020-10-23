package com.example.lyngua.controllers

import android.content.Context
import android.content.SharedPreferences
import com.example.lyngua.models.User.User
import com.google.gson.Gson

class UserController {

    fun saveInfo(context: Context, user:User){
        val sharedPref =  context.getSharedPreferences("User_pref", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString("User_object", json)
        prefsEditor.apply()
    }

    fun readUserInfo(context: Context): User?{
        val sharedPref =  context.getSharedPreferences("User_pref", Context.MODE_PRIVATE)
        val gson = Gson()
        val json: String? = sharedPref.getString("User_object", "")

        return if(json != null) {
            gson.fromJson(json, User::class.java)
        }else{
            null
        }
    }
}