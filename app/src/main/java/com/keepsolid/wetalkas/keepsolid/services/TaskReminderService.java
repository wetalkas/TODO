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
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keepsolid.wetalkas.keepsolid.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskReminderService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();



        //Toast.makeText(this, "Служюа создана", Toast.LENGTH_LONG).show();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        //Toast.makeText(this, "Напоминание", Toast.LENGTH_LONG).show();

        notifyTask();





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






    private void notifyTask() {


        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, TaskReminderService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_plus_white_18dp)
                        // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_plus_white_48dp))
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("Последнее китайское предупреждение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Напоминание")
                        //.setContentText(res.getString(R.string.notifytext))
                .setContentText("Пора покормить кота"); // Текст уведомленимя

        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);


    }



}
