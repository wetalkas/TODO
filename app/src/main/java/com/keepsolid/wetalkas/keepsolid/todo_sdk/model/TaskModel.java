package com.keepsolid.wetalkas.keepsolid.todo_sdk.model;

/**
 * Created by wetalkas on 02.06.15.
 */
public class TaskModel {

    public final String name;
    public final String description;
    public final long date;
    public final long priority;
    public boolean done;
    public final long timeStamp;



    public TaskModel(String name, String description, long date, long priority, boolean done, long timeStamp) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.done = done;
        this.timeStamp = timeStamp;

    }
}


