package com.geovis.web.util.system;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    //private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SPECIFIC_DATE_FORMAT = "yyyyMMddHHmmssSSSS";
    private static final String HOUR_MINUTIE_SCOND="HH:mm:ss";
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
        String str = sf.format(date);
        return str;
    }

    public static Date formatDateTime(String time){
        Date date=null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
            date=sf.parse(time);
        } catch (Exception e) {
        }
        return date;
    }

    public static String getCurrentYMD(){
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String str = sf.format(date);
        return str;
    }

    public  static String getSecificTypeTime(){
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat(SPECIFIC_DATE_FORMAT);
        String str = sf.format(date);
        return str;
    }

    public static  Date getDbEndTime(Date date,int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -days);
        return calendar.getTime();
    }

    public static String getHMS(Date date){
        SimpleDateFormat sf = new SimpleDateFormat(HOUR_MINUTIE_SCOND);
        String str = sf.format(date);
        return str;
    }


    public static void main11(String[] args) {


        String path ="E:/gv5/code/wangning/cjglService/cjglService/uploadData"+File.separator+"Service_OC_Algorithm.zip";


        File f = new File(path);

        System.out.println(f.getName());



    }

}

