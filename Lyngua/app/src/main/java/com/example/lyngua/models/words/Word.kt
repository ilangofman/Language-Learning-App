package com.example.lyngua.models.words

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Word(val word: String, val score: Int, val tags: List<String>) : Parcelable