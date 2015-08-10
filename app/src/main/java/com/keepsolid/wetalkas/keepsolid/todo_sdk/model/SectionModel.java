package com.keepsolid.wetalkas.keepsolid.todo_sdk.model;

/**
 * Created by wetalkas on 07.08.15.
 */
public class SectionModel implements Item {

    private int type;

    public SectionModel(int type) {
        this.type = type;
    }

    @Override
    public boolean isSection() {
        return true;
    }

    public int getType() {
        return type;
    }

    public void setType(int name) {
        this.type = name;
    }
}
