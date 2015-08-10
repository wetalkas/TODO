package com.keepsolid.wetalkas.keepsolid.sdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.util.Log;

import com.keepsolid.wetalkas.keepsolid.todo_sdk.model.TaskModel;


public class CustomSQLiteHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "reminder_database";

    public static final String DATABASE_TABLE = "tasks_table";


    public static final String USER_LOGIN_COLUMN = "user_login";
    public static final String TASK_NAME_COLUMN = "task_name";
    public static final String TASK_DESCRIPTION_COLUMN = "task_description";
    public static final String TASK_DATE_COLUMN = "task_date";
    public static final String TASK_TIME_COLUMN = "task_time";
    public static final String TASK_STATUS_COLUMN = "task_status";
    public static final String TASK_PRIORITY_COLUMN = "task_priority";



    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + USER_LOGIN_COLUMN + " text not null, " + TASK_NAME_COLUMN
            + " text not null, " + TASK_DESCRIPTION_COLUMN + " text not null, " + TASK_DATE_COLUMN
            + " long, " + TASK_TIME_COLUMN + " long, " + TASK_STATUS_COLUMN
            + " long, " + TASK_PRIORITY_COLUMN + " long);";




    private static CustomSQLiteHelper instance;


    public static CustomSQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CustomSQLiteHelper(context);
        }
        return instance;
    }






    private CustomSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DATABASE_CREATE_SCRIPT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);

    }



    public void updateString(String column, long key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);

        int count = getWritableDatabase().update(CustomSQLiteHelper.DATABASE_TABLE,
                cv, CustomSQLiteHelper.TASK_TIME_COLUMN + " = " + key, null);
        Log.d("update check", "count = " + count);
    }

    public void updateLong(String column, long key, long value) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);

        int count = getWritableDatabase().update(CustomSQLiteHelper.DATABASE_TABLE,
                cv, CustomSQLiteHelper.TASK_TIME_COLUMN + " = " + key, null);
        Log.d("update check", "count = " + count);
    }

    public void updateTask(TaskModel taskModel) {
        updateString(TASK_NAME_COLUMN, taskModel.getTimeStamp(), taskModel.getName());
        updateString(TASK_DESCRIPTION_COLUMN, taskModel.getTimeStamp(), taskModel.getDescription());
        updateLong(TASK_DATE_COLUMN, taskModel.getTimeStamp(), taskModel.getDate());
        updateLong(TASK_PRIORITY_COLUMN, taskModel.getTimeStamp(), taskModel.getPriority());
        updateLong(TASK_STATUS_COLUMN, taskModel.getTimeStamp(), taskModel.getDone());
    }






}
