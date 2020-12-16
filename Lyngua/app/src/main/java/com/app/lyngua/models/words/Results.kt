package com.app.lyngua.models.words

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Results(
    val catId: Int, val catName: String, val epochTimestamp: Long, val wrongAnsMap: MutableMap<String,String>,
    val numCorrect: Int, val numQuestions: Int) : Parcelable


