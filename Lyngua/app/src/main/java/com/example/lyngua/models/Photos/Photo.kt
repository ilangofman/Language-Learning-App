package com.example.lyngua.models.Photos

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "photo_table")
data class Photo(
    @PrimaryKey
    @ColumnInfo(name="uriString")   val uriString: String,
    @ColumnInfo(name="word")        val word: String,
): Parcelable