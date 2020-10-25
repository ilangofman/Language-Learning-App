package com.example.lyngua.models.words

import kotlin.math.pow
import kotlin.math.roundToInt
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Word(val word: String, val score: Int, val tags: List<String>) : Parcelable