package com.volkovmedia.perfo.firebaserealtimechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeManager {

    private static final String DF_GENERAL = "EEE, d MMM yyyy", DF_TIME = "HH:mm", DF_DAY = "MMM d, HH:mm";

    public static String convertTimestamp(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DF_GENERAL, Locale.ENGLISH);

        Date messageDate = new Date(timestamp),
                todayDate = new Date(System.currentTimeMillis());

        String time = dateFormat.format(messageDate),
                today = dateFormat.format(todayDate);

        dateFormat = new SimpleDateFormat(today.equals(time) ? DF_TIME : DF_DAY, Locale.ENGLISH);

        return dateFormat.format(messageDate);
    }

}