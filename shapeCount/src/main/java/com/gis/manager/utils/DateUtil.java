package com.gis.manager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author:liuyan
 * @Date:创建于2020/1/19 10:03
 * @Description:
 */
public class DateUtil {

    public static String dateformatStr="yyyy-MM-dd HH:mm:ss";

    public static SimpleDateFormat simpledDateFormat=new SimpleDateFormat(dateformatStr);

    /**
     * Date类型转换为String yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        return simpledDateFormat.format(date);
    }

    /**
     * Date类型转换为String
     * @param date
     * @param format
     * @return
     */
    public static String dateToStringFormat(Date date, String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Long类型转换为String yyyy-MM-dd HH:mm:ss
     * @param timeMillis
     * @return
     */
    public static String longToString(Long timeMillis){
        return simpledDateFormat.format(new Date(timeMillis));
    }

    /**
     * Long类型转换为String yyyy-MM-dd HH:mm:ss
     * @param timeMillis
     * @return
     */
    public static String longToStringFormat(Long timeMillis, String format){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        return sdf.format(new Date(timeMillis));
    }

    /**
     * String转Date
     * @param dataStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String dataStr){
        Date date=null;
        try {
            date=simpledDateFormat.parse(dataStr);
        } catch (ParseException e) {
            System.out.println(dataStr+"格式化日期失败!");
        }
        return date;
    }

    /**
     * String转Date
     * @param dataStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static synchronized String stringToString(String dataStr,String format){
        String res = null;
        try {
            Date date = simpledDateFormat.parse(dataStr);
            SimpleDateFormat newFormat=new SimpleDateFormat(format);
            res = newFormat.format(date);
        } catch (ParseException e) {
            System.out.println(dataStr+"格式化日期失败!");
        }
        return res;
    }

    /**
     * String转Date
     * @param dataStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static synchronized String stringToString(String dataStr,String formatSrc,String formatRes){
        String res = null;
        try {
            SimpleDateFormat simpledFormatSrc=new SimpleDateFormat(formatSrc);
            Date date = simpledFormatSrc.parse(dataStr);
            SimpleDateFormat newFormat=new SimpleDateFormat(formatRes);
            res = newFormat.format(date);
        } catch (ParseException e) {
            System.out.println(dataStr+"格式化日期失败!");
        }
        return res;
    }

    /**
     * String转Date
     * @param dataStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String dataStr, String format){
        Date date=null;
        try {
            SimpleDateFormat simpledDateFormat=new SimpleDateFormat(format);
            date=simpledDateFormat.parse(dataStr);
        } catch (ParseException e) {
            System.out.println(dataStr+"格式化日期失败!");
        }
        return date;
    }

    /**
     * 获取指定日期往后推的第n天
     * @param dataStr
     * @param format
     * @param dayNum
     * @return
     */
    public static String nextDayString(String dataStr,String format,int dayNum){
        String next = null;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat(format);
            Date date=sdf.parse(dataStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE,day+dayNum);
            next = sdf.format(calendar.getTime());
        } catch (ParseException e) {
            System.out.println(dataStr+"格式化日期失败!");
        }
        return next;
    }

    /**
     * 获取指定日期往后推的第n天
     * @param date
     * @param format
     * @return
     */
    public static String nextDayDate(Date date,String format,int dayNum){
        SimpleDateFormat sdf=new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,day+dayNum);
        String next = sdf.format(calendar.getTime());
        return next;
    }


}
