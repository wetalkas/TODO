package com.keepsolid.wetalkas.keepsolid.sdk;

import android.content.Context;
import android.util.Log;

import com.keepsolid.wetalkas.keepsolid.R;
import com.keepsolid.wetalkas.keepsolid.todo_sdk.Constants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wetalkas on 30.05.15.
 */
public class Sdk {

    public static String md5Custom(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }




    public static String getDateWithCurrentLocale(long initialDate, Context context) {
        Locale currentLocale = context.getResources().getConfiguration().locale;

        Date date = new Date(initialDate); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm", currentLocale); // the format of your date
        //sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        return sdf.format(initialDate);
    }



    public static String getSeparatorName(int type, Context context) {
        String name = "";

        switch (type) {
            case Constants.STATUS_OVERDUE:
                name = context.getString(R.string.separator_overdue);
                Log.d("case day", "overdue");
                break;
            case Constants.STATUS_TODAY:
                name = context.getString(R.string.separator_today);
                Log.d("case day", "today");
                break;
            case Constants.STATUS_TOMORROW:
                name = context.getString(R.string.separator_tomorrow);
                Log.d("case day", "tomorrow");
                break;
            case Constants.STATUS_FUTURE:
                name = context.getString(R.string.separator_future);
                Log.d("case day", "future");
                break;
        }


        return name;
    }




}
