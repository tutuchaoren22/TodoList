package com.duyuqian.todolist.others;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.duyuqian.todolist.MyApplication;

import java.util.Calendar;
import java.util.Date;

public class AlarmUtil {
    private Context context;
    private MyNotification myNotification;

    public AlarmUtil() {
        this.context = MyApplication.getInstance().getApplicationContext();
        myNotification = new MyNotification(context);
    }

    public void addNotification(int id, String title, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, TodoListConstant.ALARM_HOUR);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("id", id);
        intent.setAction(TodoListConstant.INTENT_NOTIFICATION_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void cancelNotificationById(int id) {
        myNotification.cancelNotificationById(id);
    }
}
