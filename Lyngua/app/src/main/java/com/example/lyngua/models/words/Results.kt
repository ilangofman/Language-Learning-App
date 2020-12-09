package com.example.lyngua.models.words

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.lyngua.models.goals.Goal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Results(
    val catId: Int, val catName: String, val epochTimestamp: Long, val wrongAnsMap: MutableMap<String,String>,
    val numCorrect: Int, val numQuestions: Int) : Parcelable


