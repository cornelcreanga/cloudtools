package com.com.ccreanga.cloudutils.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static String formatDate(Date date){
        return formatter.format(Instant.ofEpochMilli(date.getTime()).atOffset( ZoneOffset.UTC ));
    }



}
