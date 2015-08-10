package com.keepsolid.wetalkas.keepsolid.sdk;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by wetalkas on 21.04.15.
 */
public class CustomPreferenceManager {

    private static CustomPreferenceManager instance;

    private Context context;


    private CustomPreferenceManager() {

    }






    public static CustomPreferenceManager getInstance() {

        if (instance == null) {
            instance = new CustomPreferenceManager();
        }

        return instance;
    }


    public void init(Context context){
        this.context = context;
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


    public boolean getBoolean(String key) {
        return context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE).getBoolean(key, false);
    }


    public void putBoolean(String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("ToDoList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);
        editor.apply();
    }



}
