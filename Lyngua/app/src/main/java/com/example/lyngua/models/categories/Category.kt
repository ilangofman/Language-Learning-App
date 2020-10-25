package com.example.lyngua.models.categories

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.lyngua.models.words.Word
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")          val id:Int,
    @ColumnInfo(name="name")        val name: String,
    @ColumnInfo(name="numWords")    val numWords:Int,
    @ColumnInfo(name="wordsList")   val wordsList: List<Word>,
    @ColumnInfo(name="sessionNumber") var sessionNumber:Int
): Parcelable

class WordsTypeConverter {
    @TypeConverter
    fun toWordList(jsonString: String?): List<Word>{
        return Gson().fromJson(jsonString, object :TypeToken<List<Word>>(){}.type)
    }

    @TypeConverter
    fun fromWordList(wordList: List<Word?>): String{
        return Gson().toJson(wordList)
    }
}