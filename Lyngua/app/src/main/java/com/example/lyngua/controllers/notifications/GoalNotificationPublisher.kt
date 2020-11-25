package com.example.lyngua.controllers.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.lyngua.R
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.goals.Goal
import com.example.lyngua.views.*
import com.example.lyngua.views.Categories.Practice
import com.example.lyngua.views.Categories.UpdateCategory.SwitchType.SWITCH_ON
import java.util.*

//Notifications are set up using tutorials from https://www.youtube.com/watch?v=B5dgmvbrHgs
// and https://developer.android.com/training/notify-user/build-notification
class GoalNotificationPublisher() : BroadcastReceiver() {

    private var context: Context? = null
    private val CHANNEL_ID = "ReminderID1"

    override fun onReceive(context: Context, intent: Intent?) {

        this.context = context

        //Unbundle the information for the category to receive a notification from the intent
        val bundle = intent?.getBundleExtra("bundle")
        var currentCategory = bundle?.getParcelable<Category>("category")
        var currentGoal = bundle?.getParcelable<Goal>("goal")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Category Reminder"
            val descriptionText = "Goal due date"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = R.color.colorPrimary
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (currentCategory != null && currentGoal != null) {
                sendNotification(currentCategory, currentGoal)
        }

    }

    private fun sendNotification(currentCategory: Category, currentGoal: Goal) {

        var bundle = Bundle()
        //Toast.makeText(context, "Category is ${category.name}", Toast.LENGTH_LONG).show()
        bundle.putParcelable("Practice", currentCategory)

        //Build intent to bring the user to the practice mode fragment
        val pendingIntent = NavDeepLinkBuilder(this.context!!)
            .setComponentName(ActivityTabs::class.java)
            .setGraph(R.navigation.main_navigation)
            .setDestination(R.id.practice)
            .setArguments(bundle)
            .createPendingIntent()

        //Sets message based on type of goal set
        var message: String
        if (currentGoal.goalType == SWITCH_ON){
            message = "You have a word goal ending soon!"
        }
        else{
            message = "You have a time goal ending soon!"
        }

        val GROUP_GOAL_NOTIFICATION = "GOAL_NOTIFICATION"

        //Builds the notification and its components
        val builder = NotificationCompat.Builder(this.context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_notif_light)
            .setColor(ContextCompat.getColor(this.context!!, R.color.colorPrimary))
            .setContentTitle("${currentCategory.name.capitalize()} Category")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(GROUP_GOAL_NOTIFICATION)
            .setContentIntent(pendingIntent)
            .build()

        with(NotificationManagerCompat.from(this.context!!)){
            notify(currentCategory.id,builder)
        }
    }

}