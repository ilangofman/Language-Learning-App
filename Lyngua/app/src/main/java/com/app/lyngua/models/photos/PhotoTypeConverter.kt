package com.app.lyngua.models.photos

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhotoTypeConverter {
    @TypeConverter
    fun toList(jsonString: String?): List<String> {
        return Gson().fromJson(jsonString, object :TypeToken<List<String>>(){}.type)
    }

    @TypeConverter
    fun fromList(wordList: List<String>): String {
        return Gson().toJson(wordList)
    }
}