package com.keepsolid.wetalkas.keepsolid.todo_sdk;

import com.keepsolid.wetalkas.keepsolid.R;

/**
 * Created by wetalkas on 31.05.15.
 */
public class Constants {

    public static final String[] PRIORITY_LEVELS = {"Low Priority", "Normal Priority", "High Priority",
            "Very High Priority"};

    public static final String[] SORTING_BY = {"Date", "Name", "Priority", "Status"};

    public static final int[] PRIORITY_COLORS = {R.color.priority_very_high, R.color.priority_high,
            R.color.priority_normal, R.color.priority_low};

    public static final int[] PRIORITY_COLORS_CLICK = {R.color.priority_very_high_click, R.color.priority_high_click,
            R.color.priority_normal_click, R.color.priority_low_click};



    public static final int STATUS_DONE = 0;
    public static final int STATUS_OVERDUE = 1;
    public static final int STATUS_TODAY = 2;
    public static final int STATUS_TOMORROW = 3;
    public static final int STATUS_FUTURE = 4;



}
