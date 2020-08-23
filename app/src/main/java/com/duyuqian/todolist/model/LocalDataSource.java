package com.duyuqian.todolist.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.duyuqian.todolist.others.TodoListConstant;

@Database(entities = {User.class}, version = 1)
public abstract class LocalDataSource extends RoomDatabase {
    private static LocalDataSource localDataSource;

    public static LocalDataSource getInstance(Context context) {
        if (localDataSource == null) {
            localDataSource = Room.databaseBuilder(context,
                    LocalDataSource.class, TodoListConstant.DATA_BASE_NAME)
                    .build();
        }
        return localDataSource;
    }

    public abstract UserDao userDao();
}
