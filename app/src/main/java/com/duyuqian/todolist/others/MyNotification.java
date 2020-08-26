package com.duyuqian.todolist.others;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.duyuqian.todolist.R;
import com.duyuqian.todolist.view.HomeActivity;

import java.util.Calendar;

public class MyNotification {
    public static final int NOTIFY_ID = 1;
    private NotificationManagerCompat notificationManager;

    public MyNotification(Context context) {
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    public void sendNotification(Context context) {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(TodoListConstant.CHANNEL_ID, TodoListConstant.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TodoListConstant.CHANNEL_ID);
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = builder.setSmallIcon(R.drawable.ic_todo_list)
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setContentTitle(TodoListConstant.NOTIFICATION_TITLE)
                .setContentText(TodoListConstant.NOTIFICATION_CONTENT)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void cancelAllNotification() {
        notificationManager.cancelAll();
    }

}
