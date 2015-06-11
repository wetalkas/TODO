package com.keepsolid.wetalkas.keepsolid.services;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.IBinder;


import com.keepsolid.wetalkas.keepsolid.R;


public class TaskReminderService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();



        //Toast.makeText(this, "Служюа создана", Toast.LENGTH_LONG).show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //Toast.makeText(this, "Напоминание", Toast.LENGTH_LONG).show();

        notifyTask(intent);





        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public TaskReminderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }






    private void notifyTask(Intent intent) {

        String text = "Remind";
        if (intent != null) {
            text = intent.getStringExtra("task_title");
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
