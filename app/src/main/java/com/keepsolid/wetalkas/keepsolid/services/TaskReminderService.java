package com.keepsolid.wetalkas.keepsolid.services;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;


import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.activities.MainActivity;
import com.keepsolid.wetalkas.keepsolid.fragments.TasksFragment;
import com.keepsolid.wetalkas.keepsolid.sdk.CustomSQLiteHelper;

import java.util.Random;
import java.util.RandomAccess;


public class TaskReminderService extends Service {


    CustomSQLiteHelper customSQLiteHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "onCreate");








    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*if (intent != null) {
            notifyTask(intent);
        }*/
        Log.d("service", "onStartCommand");




        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);


        Notification noti_builder = new Notification.Builder(this)
                .setContentTitle(intent.getStringExtra("task_title"))
                .setSmallIcon(R.drawable.ic_plus_white_24dp)
                .setContentIntent(pIntent)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //what does this do!?



        noti_builder.flags |= Notification.FLAG_AUTO_CANCEL;
        noti_builder.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;


        long timeStamp = intent.getLongExtra("task_time", 0);



        notificationManager.notify((int) timeStamp, noti_builder);



        customSQLiteHelper = CustomSQLiteHelper.getInstance(getApplicationContext());


        customSQLiteHelper.updateLong(CustomSQLiteHelper.TASK_STATUS_COLUMN, timeStamp, 1);





        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public TaskReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }






    private void notifyTask(Intent intent) {

        String text = "Remind";
        if (intent != null) {
            text = intent.getStringExtra("task_title");
        } else {
            Log.d("service", "null");
        }

        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, TaskReminderService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);


        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_clock_white_24dp)
                        // большая картинка
                        //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_clock_white_36dp))
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker(text)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(getString(R.string.app_name))
                        //.setContentText(res.getString(R.string.notifytext))
                .setContentText(text); // Текст уведомленимя



        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);


    }



}
