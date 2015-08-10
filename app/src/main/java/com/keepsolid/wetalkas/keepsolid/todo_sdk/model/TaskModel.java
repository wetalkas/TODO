package com.keepsolid.wetalkas.keepsolid.todo_sdk.model;

/**
 * Created by wetalkas on 02.06.15.
 */
public class TaskModel implements Item {

    private String name;
    private String description;
    private long date;
    private long priority;
    private long done;
    private long timeStamp;
    private boolean doneBool;


    public TaskModel() {

    }


    public TaskModel(String name, String description, long date, long priority, long done, long timeStamp) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.done = done;
        this.timeStamp = timeStamp;
        this.doneBool = done == 0;
    }

    public void setAll(TaskModel newTask) {
        this.name = newTask.name;
        this.description = newTask.description;
        this.date = newTask.date;
        this.priority = newTask.priority;
        this.done = newTask.done;
        this.timeStamp = newTask.timeStamp;
        this.doneBool = newTask.doneBool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public long getDone() {
        return done;
    }

    public void setDone(long done) {
        this.done = done;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isDoneBool() {
        return doneBool;
    }

    public void setDoneBool(boolean doneBool) {
        this.doneBool = doneBool;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}


