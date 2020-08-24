package com.duyuqian.todolist.model.task;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @NonNull
    @PrimaryKey
    private int id;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo(name = "has_done")
    private boolean hasDone;
    @ColumnInfo(name = "is_reminded")
    private boolean isReminded;
    @ColumnInfo(name = "has_done")
    private String dateOfRemind;

    public Task(String title, String description, boolean hasDone, boolean isReminded, String dateOfRemind) {
        this.title = title;
        this.description = description;
        this.hasDone = hasDone;
        this.isReminded = isReminded;
        this.dateOfRemind = dateOfRemind;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isHasDone() {
        return hasDone;
    }

    public boolean isReminded() {
        return isReminded;
    }

    public String getDateOfRemind() {
        return dateOfRemind;
    }
}
