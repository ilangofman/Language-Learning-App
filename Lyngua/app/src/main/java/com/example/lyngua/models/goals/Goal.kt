package com.example.lyngua.models.goals

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.example.lyngua.controllers.notifications.AlarmService
import com.example.lyngua.controllers.notifications.GoalNotificationPublisher
import com.example.lyngua.controllers.notifications.GoalUpdatePublisher
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
    var timeSpent: Double,
    var totalTime: Double

) : Parcelable {

    fun updateGoal() {
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
        timeSpent = 0.0

    }

    fun cancelAlarms(context: Context, category: Category) {
        val mAlarmSender: PendingIntent
        val alarmGoalSender: PendingIntent
        var broadcastIntent: Intent = Intent(context, GoalNotificationPublisher::class.java)
        var broadcastIntent1: Intent = Intent(context, GoalUpdatePublisher::class.java)

        //Create bundle to send category and goal information to the broadcast receiver
        var bundle = Bundle()
        bundle.putParcelable("category", category)
        bundle.putParcelable("goal", category.goal)
        broadcastIntent.putExtra("bundle", bundle)
        broadcastIntent1.putExtra("bundle", bundle)

        //Create pending intent to be able to cancel the alarms set for the specific category
        mAlarmSender =
            PendingIntent.getBroadcast(
                context,
                category.id,
                broadcastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        alarmGoalSender =
            PendingIntent.getBroadcast(
                context,
                category.id + 1000,
                broadcastIntent1,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("ALARMS", "CANCELLED")
        am.cancel(mAlarmSender)
        am.cancel(alarmGoalSender)
    }
}
