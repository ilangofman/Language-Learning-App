package com.example.lyngua.models.words

import android.os.Parcelable
import com.example.lyngua.models.Question
import com.example.lyngua.models.categories.Category
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class GameSessionData (
    var questionList: @RawValue ArrayList<Question>,
    var categoryChosen: Category,
    var numWordsDone: Int = 0,
    var numCorrect: Int = 0,
    var wrongAnsMap: MutableMap<String,String>,
    var currentQuestionPos: Int = 0) : Parcelable
