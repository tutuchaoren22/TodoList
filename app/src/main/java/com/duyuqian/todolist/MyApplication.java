package com.duyuqian.todolist;

import android.app.Application;

import androidx.room.Room;

import com.duyuqian.todolist.model.LocalDataSource;

public class MyApplication extends Application {
    public static MyApplication myApplication;
    public static LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public LocalDataSource getLocalDataSource() {
        localDataSource = Room.databaseBuilder(getApplicationContext(),
                LocalDataSource.class, "dataBase")
                .build();
        return localDataSource;
    }
}
