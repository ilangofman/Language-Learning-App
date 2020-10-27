package com.example.lyngua.models.Albums

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "category_table")
data class Album(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="name")        val name: String,
    @ColumnInfo(name="coverPhoto")  val coverPhoto: String?,
    @ColumnInfo(name="isEmpty")     val isEmpty: Boolean
): Parcelable