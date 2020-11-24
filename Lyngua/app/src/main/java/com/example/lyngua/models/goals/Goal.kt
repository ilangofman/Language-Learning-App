package com.example.lyngua.models.goals

import android.content.Context
import android.os.Parcelable
import com.example.lyngua.controllers.notifications.AlarmService
import com.example.lyngua.models.categories.Category
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class Goal(
    var time: Calendar,
    var timeFrame: Int,
    var notificationFlag: Int,
    var goalType: Int,
    var numWordsCompleted: Int,
    var totalNumWords: Int,
    var timeSpent: Int,
    var totalTime: Int

): Parcelable {

    fun updateGoal(){
        val myCalendar = Calendar.getInstance()
        when (this.timeFrame) {
            0 -> Calendar.getInstance()
            1 -> myCalendar.add(Calendar.SECOND, 60)
            2 -> myCalendar.add(Calendar.DAY_OF_MONTH, 1)
            3 -> myCalendar.add(Calendar.DAY_OF_MONTH, 7)
            4 -> myCalendar.add(Calendar.MONTH, 1)
        }

        time = myCalendar
        numWordsCompleted = 0
        timeSpent = 0

    }
}
