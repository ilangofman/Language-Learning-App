package com.example.lyngua.controllers.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.goals.Goal
import java.util.*


class AlarmService(context: Context, category: Category, goal: Goal) {
    private val context: Context
    private val mAlarmSender: PendingIntent
    private val alarmGoalSender : PendingIntent

    val category = category
    val goal = goal

    val notifCalendar = Calendar.getInstance()
    val c : Calendar = goal.time
    fun startAlarm() {

        //Sets the proper time to send the notification for goal
        when (goal.timeFrame) {
            -1 -> Calendar.getInstance()
            0 -> notifCalendar.add(Calendar.SECOND, 30)
            1 -> notifCalendar.add(Calendar.HOUR, 18)
            2 -> notifCalendar.add(Calendar.DAY_OF_MONTH, 6)
            3 -> notifCalendar.add(Calendar.DAY_OF_MONTH, 26)
        }

        val firstTime: Long = c.getTimeInMillis()
        val notifTime: Long = notifCalendar.getTimeInMillis()
        val intervalTime: Long = firstTime - notifCalendar.getTimeInMillis()

        //Create the alarm for notification if the checkbox was enabled
        if(goal.notificationFlag == 1) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.set(AlarmManager.RTC_WAKEUP, notifTime, mAlarmSender)
        }

        //Schedule alarm for finishing goal
        val am2 = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am2.setRepeating(AlarmManager.RTC_WAKEUP, firstTime, intervalTime, alarmGoalSender)
        //am2.set(AlarmManager.RTC_WAKEUP, firstTime, alarmGoalSender)

    }

    init {
        this.context = context
        var broadcastIntent: Intent = Intent(context, BroadcastManager::class.java)
        var broadcastIntent1: Intent = Intent(context, AlarmGoal::class.java)

        //Create bundle to send category and goal information to the broadcast receiver
        var bundle = Bundle()
        bundle.putParcelable("category", category)
        bundle.putParcelable("goal", goal)
        broadcastIntent.putExtra("bundle", bundle)
        broadcastIntent1.putExtra("bundle", bundle)

        mAlarmSender =
            PendingIntent.getBroadcast(context, category.id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmGoalSender =
            PendingIntent.getBroadcast(context, category.id+1000, broadcastIntent1, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}