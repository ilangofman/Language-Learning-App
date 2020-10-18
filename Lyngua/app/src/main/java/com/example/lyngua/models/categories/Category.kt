package com.example.lyngua.models.categories

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val name: String,
    val numWords:Int,
    val wordsList: Array<Word>
): Parcelable
