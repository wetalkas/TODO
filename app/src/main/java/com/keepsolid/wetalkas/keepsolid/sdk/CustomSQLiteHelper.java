package com.keepsolid.wetalkas.keepsolid.sdk;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class CustomSQLiteHelper extends SQLiteOpenHelper {


    public static final String DATABASE_VERSION = "2";




    private static final String DATABASE_TABLE = "tasks";


    public static final String TASK_NAME_COLUMN = "task_name";
    public static final String TASK_DESCRIPTION_COLUMN = "task_description";
    public static final String TASK_DATE_COLUMN = "task_date";
    public static final String TASK_TIME_COLUMN = "task_time";
    public static final String TASK_STATUS_COLUMN = "task_status";

    public static final String TASK_PRIORITY_COLUMN = "task_priority";


    private static final String DATABASE_CREATE_SCRIPT = "create table "
            + DATABASE_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + TASK_NAME_COLUMN
            + " text not null, " + TASK_DESCRIPTION_COLUMN + " text not null, " + TASK_DATE_COLUMN
            + " long, " + TASK_TIME_COLUMN + " long, " + TASK_STATUS_COLUMN
            + " boolean, " + TASK_PRIORITY_COLUMN + " long);";




    public CustomSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


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
}
