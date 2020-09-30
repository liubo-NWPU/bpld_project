package com.gis.trans.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    //private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String XML_DATE_FORMAT = "yyyyMMdd";

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String str = sf.format(date);
        return str;
    }

    public static Date formatDateTime(String time) {
        Date date = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
            date = sf.parse(time);
        } catch (Exception e) {
        }
        return date;
    }

    public static String getDateTime(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String str = sf.format(date);
        return str;
    }

    public static String getXmlDateFormat(Date date) {
        //Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(XML_DATE_FORMAT);
        String str = sf.format(date);
        return str;
    }

    public static Date getDbEndTime(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    public static Long getNetTime(Long date){
        return date * 10000 + 621356256000000000l;
    }

}
