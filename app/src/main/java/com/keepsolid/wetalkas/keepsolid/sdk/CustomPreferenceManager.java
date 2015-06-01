package com.keepsolid.wetalkas.keepsolid.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.keepsolid.wetalkas.keepsolid.activities.ScrollActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wetalkas on 21.04.15.
 */
public class CustomPreferenceManager {

    private static CustomPreferenceManager instance;

    private Context context;
    private String userLogin;


    private CustomPreferenceManager() {

    }



    public static CustomPreferenceManager getInstance() {

        if (instance == null) {
            instance = new CustomPreferenceManager();
        }

        return instance;
    }


    public void init(Context context, String userLogin){
        this.context = context;
        this.userLogin = userLogin;
    }


    public String getString(String key) {


        return context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE).getString(key, "");

    }

    public void putString(String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);
        editor.apply();






    }




    public void putTaskModel(List<ScrollActivity.TaskItem> items) {
        SharedPreferences preferences = context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();



        int count = items.size();

        editor.putInt("Tasks Count", count);

        Set<String> modelSet = getStringSetFromModel(items.get(count - 1));

        editor.putStringSet("Tasks List " + count, modelSet);


        Log.d("Set of tasks", Arrays.toString(modelSet.toArray()));


        //Log.d("prefmanager", "Tasks List " + count);



        editor.apply();

        Set<String> set = new LinkedHashSet<>();

        set.addAll(preferences.getStringSet("Tasks List " + count, null));

        Log.d("Set shared", Arrays.toString(set.toArray()));




    }


    public LinkedHashSet<String> getStringSetFromModel(ScrollActivity.TaskItem item) {
        LinkedHashSet<String> model = new LinkedHashSet<>();

        model.add(item.name);
        //model.add(item.date);
        model.add(item.description);


        Log.d("set of omdel", Arrays.toString(model.toArray()));





        return model;
    }




    public List<ScrollActivity.TaskItem> getTaskList(String key) {
        List<ScrollActivity.TaskItem> items = new ArrayList<>();

        SharedPreferences preferences = context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE);




        int count = preferences.getInt("Tasks Count", 0);


        for (int i = 0; i < count; i++) {

            int k = i + 1;

            Set<String> tasksSet = preferences.getStringSet("Tasks List " + k, null);

            Log.d("set item", Arrays.toString(tasksSet.toArray()));

            ScrollActivity.TaskItem task = getTaskFromSet(tasksSet);
            items.add(task);
        }






        return items;
    }

    public int getCount() {

        SharedPreferences preferences = context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE);




        return preferences.getInt("Tasks Count", 0);
    }




    public ScrollActivity.TaskItem getTaskFromSet(Set<String> modelSet) {
        ScrollActivity scrollActivity = new ScrollActivity();

        ScrollActivity.TaskItem taskItem = scrollActivity.getNewTask("", 0, "");



        Log.d("model set", Arrays.toString(modelSet.toArray()));


        if (!modelSet.isEmpty()) {
            String name = (String) modelSet.toArray()[0];
            String date = (String) modelSet.toArray()[1];
            String description = (String) modelSet.toArray()[2];


            //taskItem = scrollActivity.getNewTask(name, date, description);
        }


        return taskItem;
    }


    public void removeAllTasks() {

    }




}
