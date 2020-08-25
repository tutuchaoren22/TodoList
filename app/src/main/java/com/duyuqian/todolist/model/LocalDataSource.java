package com.duyuqian.todolist.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.duyuqian.todolist.model.task.Task;
import com.duyuqian.todolist.model.task.TaskDao;
import com.duyuqian.todolist.model.user.User;
import com.duyuqian.todolist.model.user.UserDao;
import com.duyuqian.todolist.others.TodoListConstant;

@Database(entities = {User.class, Task.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
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

    public abstract TaskDao taskDao();
}
