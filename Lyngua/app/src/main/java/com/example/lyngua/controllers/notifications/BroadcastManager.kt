package com.example.lyngua.controllers.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.lyngua.R
import com.example.lyngua.models.categories.Category
import com.example.lyngua.models.goals.Goal
import com.example.lyngua.views.*
import java.util.*

//Notifications are set up using tutorials from https://www.youtube.com/watch?v=B5dgmvbrHgs
// and https://developer.android.com/training/notify-user/build-notification
class BroadcastManager() : BroadcastReceiver() {

    private var context: Context? = null
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    override fun onReceive(context: Context, intent: Intent?) {

        this.context = context

        //Unbundle the information for the category to receive a notification from the intent
        val bundle = intent?.getBundleExtra("bundle")
        var currentCategory = bundle?.getParcelable<Category>("category")
        var currentGoal = bundle?.getParcelable<Goal>("goal")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        if (currentCategory != null && currentGoal != null) {
                sendNotification(currentCategory, currentGoal)
        }

    }

    private fun sendNotification(currentCategory: Category, currentGoal: Goal) {
        // Create the explicit intent to send a notification
        val intent = Intent(this.context, More::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        var pendingIntent: PendingIntent = getActivity(this.context, 0, intent, 0)
        val date = Calendar.getInstance() //MAYBE NOT USED

        //Builds the notification and its components
        val builder = NotificationCompat.Builder(this.context!!, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) //TODO change to different logo potentially
            .setContentTitle("${currentCategory.name.capitalize()} Category")
            .setContentText("You have ${currentGoal.totalNumWords - currentCategory.goal.numWordsCompleted} words left to complete")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(this.context!!)){
            notify(notificationId,builder.build())
        }
    }
}