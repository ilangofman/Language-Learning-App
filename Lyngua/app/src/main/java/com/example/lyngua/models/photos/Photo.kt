package com.example.lyngua.models.photos

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class Photo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")          val id:Int,
    @ColumnInfo(name="uriString")   var uriString: String,
    @ColumnInfo(name="word")        val word: String,
    @ColumnInfo(name="options")     val options: List<String>,
    @ColumnInfo(name="correct")     val correct: Int,
): Parcelable