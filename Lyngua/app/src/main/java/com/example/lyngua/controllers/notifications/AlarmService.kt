package com.example.lyngua.controllers.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.goals.Goal
import java.text.DateFormatSymbols
import java.util.*


class AlarmService(context: Context, category: Category, goal: Goal) {
    private val context: Context
    private val mAlarmSender: PendingIntent
    val c : Calendar = goal.time
    fun startAlarm() {
        //Set the alarm to 10 seconds from now
        //val c: Calendar = Calendar.getInstance()
        //c.add(Calendar.SECOND, 10)
        val firstTime: Long = c.getTimeInMillis()
        // Schedule the alarm!
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        //Toast.makeText(context, "month of ${DateFormatSymbols().months[c.get(Calendar.MONTH)]} day of ${c.get(Calendar.DAY_OF_MONTH)}", Toast.LENGTH_SHORT).show()
        am[AlarmManager.RTC_WAKEUP, firstTime] = mAlarmSender
    }

    init {
        this.context = context
        var broadcastIntent: Intent = Intent(context, BroadcastManager::class.java)

        //Create bundle to send category and goal information to the broadcast receiver
        var bundle = Bundle()
        bundle.putParcelable("category", category)
        bundle.putParcelable("goal", goal)
        broadcastIntent.putExtra("bundle", bundle)
        mAlarmSender =
            PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}