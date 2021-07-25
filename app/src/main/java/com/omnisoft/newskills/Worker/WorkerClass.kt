package com.omnisoft.newskills.Worker

import android.app.*
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.icu.util.VersionInfo
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.impl.model.Preference
import com.omnisoft.newskills.MainActivity
import com.omnisoft.newskills.R
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class WorkerClass(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        createNotification()
//        setForegroundAsync(createNotification())
        return Result.success()
    }

    private fun createNotification() {
        val notificationManager : NotificationManager = applicationContext.getSystemService(NotificationManager::class.java)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("channel_1","channel_1",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "channel_1"
            notificationManager.createNotificationChannel(notificationChannel)
        }
        applicationContext.getSharedPreferences("PREFS",MODE_PRIVATE).edit()
            .putString("triggered",SimpleDateFormat("hh:mm:ss",Locale.ENGLISH).format(Date(System.currentTimeMillis())))
            .putInt("count",applicationContext.getSharedPreferences("PREFS", MODE_PRIVATE).getInt("count",0)+1).apply()

        val doAction = Intent(applicationContext,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext,0,doAction,PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManagerCompat:NotificationManagerCompat= NotificationManagerCompat.from(applicationContext)
        val notification = NotificationCompat.Builder(applicationContext,"channel_1")
            .setAutoCancel(true)
            .setShowWhen(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(pendingIntent)
            .setContentTitle("Alarm")
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentText("Time to wake up")
//        return ForegroundInfo(1,notification.build())
        notificationManagerCompat.notify(1,notification.build())
    }
}