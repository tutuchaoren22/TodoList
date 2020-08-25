package com.duyuqian.todolist.repository;

import com.duyuqian.todolist.MyApplication;
import com.duyuqian.todolist.model.LocalDataSource;
import com.duyuqian.todolist.model.task.Task;

import java.util.List;

public class TaskRepository {
    private LocalDataSource dataBase = LocalDataSource.getInstance(MyApplication.getInstance());

    public List<Task> getAllTask() {
        return dataBase.taskDao().getAll();
    }

    public void insertTask(Task task) {
        dataBase.taskDao().insertAll(task);
    }

    public void updateTaskList(Task task) {
        dataBase.taskDao().updateTask(task);
    }
}
