package com.example.lyngua.models.words

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Results(val wrongAnsMap: MutableMap<String,String>,
                   val numCorrect: Int, val numQuestions: Int) : Parcelable