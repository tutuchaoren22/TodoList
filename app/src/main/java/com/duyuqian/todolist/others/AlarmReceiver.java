package com.duyuqian.todolist.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), TodoListConstant.INTENT_NOTIFICATION_ACTION)) {
            Log.e("TAG", "onReceive: ");
            MyNotification myNotification = new MyNotification(context);
            myNotification.sendNotification(context);
        }
    }
}
