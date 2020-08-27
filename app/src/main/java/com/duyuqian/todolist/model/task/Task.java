package com.duyuqian.todolist.model.task;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "task")
public class Task implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String description;
    @ColumnInfo(name = "has_done")
    private boolean hasDone;
    @ColumnInfo(name = "is_reminded")
    private boolean isReminded;
    @ColumnInfo(name = "date_of_remind")
    private Date dateOfRemind;

    public Task(String title, String description, boolean hasDone, boolean isReminded, Date dateOfRemind) {
        this.title = title;
        this.description = description;
        this.hasDone = hasDone;
        this.isReminded = isReminded;
        this.dateOfRemind = dateOfRemind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasDone() {
        return hasDone;
    }

    public void setHasDone(boolean hasDone) {
        this.hasDone = hasDone;
    }

    public boolean isReminded() {
        return isReminded;
    }

    public void setReminded(boolean reminded) {
        isReminded = reminded;
    }

    public Date getDateOfRemind() {
        return dateOfRemind;
    }

    public void setDateOfRemind(Date dateOfRemind) {
        this.dateOfRemind = dateOfRemind;
    }
}
