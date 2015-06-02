package com.keepsolid.wetalkas.keepsolid.todo_sdk.model;

/**
 * Created by wetalkas on 02.06.15.
 */
public class TaskModel {

    public final String name;
    public final long date;
    public final String description;
    public final boolean done;
    public final long timeStamp;



    public TaskModel(String name, String description, long date, boolean done, long timeStamp) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.done = done;
        this.timeStamp = timeStamp;

    }
}


