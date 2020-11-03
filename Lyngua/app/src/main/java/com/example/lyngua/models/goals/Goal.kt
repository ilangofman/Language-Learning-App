package com.example.lyngua.models.goals

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
open class Goal(
    open var numWordsCompleted: Int,
    open var time: Calendar,
    open var timeFrame: Int,
    open var notificationFlag: Int,
    open var goalType: Int,
    open var totalNumWords: Int

): Parcelable

//TODO Maybe use for when having different types of goals
@Parcelize
class DeadLineGoal(
    override  var numWordsCompleted: Int,
    override var time: Calendar,
    override var timeFrame: Int,
    override var notificationFlag: Int,
    override var goalType: Int,
    override var totalNumWords: Int
) : Goal(numWordsCompleted, time, timeFrame,notificationFlag, goalType, totalNumWords)

@Parcelize
class TimeFrameGoal(
    override  var numWordsCompleted: Int,
    override var time: Calendar,
    override var timeFrame: Int,
    override var notificationFlag: Int,
    override var goalType: Int,
    override var totalNumWords: Int
) : Goal(numWordsCompleted, time, timeFrame,notificationFlag, goalType, totalNumWords)