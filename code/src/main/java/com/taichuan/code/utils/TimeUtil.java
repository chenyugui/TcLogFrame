package com.taichuan.code.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 此类编写了各种String和Date型的各种格式转换
 *
 * @author gui
 */
public class TimeUtil {
    private static Calendar calendar;

    /**
     * 获取时间差mm:ss
     *
     * @param start 开始时间，格式： yyyy-MM-dd HH:mm:ss
     * @param end   结束时间，格式： yyyy-MM-dd HH:mm:ss
     * @return 时间差： 格式: mm:ss
     */
    public static String getTimeDiff(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置显示格式
        try {
            Date d1 = df.parse(start);
            Date d2 = df.parse(end);
            long diff = d2.getTime() - d1.getTime();
            long day = diff / (24 * 60 * 60 * 1000);
            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            String mm = String.valueOf(min);
            if (mm.length() < 2) {
                mm = "0" + mm;
            }
            String ss = String.valueOf(s);
            if (ss.length() < 2) {
                ss = "0" + ss;
            }

            return (mm + ":" + ss);
        } catch (Exception e) {
            e.printStackTrace();
            return "--:--";
        }
    }

    /**
     * 将Long型 毫秒 转换成 String 型 yy-MM-dd
     */

//    public static String longToyyMMdd(Long millisecond) {
//        Date date = new Date(millisecond);
//        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//        return sd.format(date);
//    }

    /**
     * 获取今年一月一号0点的时间戳
     */
    public static long getTimeStampOfFirstDayOfYear() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取今天最后一天23点59分59秒的时间戳
     */
    public static long getTimeStamp24OfLastDayOfYear() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本月1号0点的时间戳
     */
    public static long getTimeStampOfFirstDayOfMonth() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本月最后一天23点59分59秒的时间戳
     */
    public static long getTimeStamp24OfLastDayOfMonth() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本周第一天0点的时间戳
     */

    public static long getTimeStampOfFirstDayOfWeek() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本周最后一天23点59分59秒的时间戳
     */

    public static long getTimeStamp24OfLastDayOfWeek() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取今天0点的时间戳
     */

    public static long getTimeStampOfZeroOfToday() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 获取今天0点的时间戳
     */
    public static long getTimeStamp24OfToday() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }

    /**
     * 获取本月第指定周的最后一天的23点59分59秒的时间戳
     *
     * @param whichWeek 第几周
     */
    public static long getTimeStamp24OfLastDayOfWhichWeekOfMonth(int whichWeek) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_MONTH, whichWeek);
        calendar.set(Calendar.DAY_OF_WEEK, 7);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime().getTime();
    }


    /**
     * 获取本月指定周的第一天0点的时间戳，例如传2，表示获取本月第二个星期的第一天
     */
    public static long getTimeStampOfFirstDayOfWhichWeekOfMonth(int i) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_MONTH, i);
        calendar.set(Calendar.DAY_OF_WEEK, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * yy-MM-dd HH:mm:ss
     */
    public static Date stringtoDate(String dateString) {
        Date date;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//大写H为24小时制,小写为12小时
            date = dateformat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String变为date的yy-MM-dd
     */
    public static Date stringToyy_MM_dd(String str) {
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd");
            return dateformat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String变为date的yy-MM-dd
     */
    public static Date stringToyyyy_MM_dd(String str) {
        Date date;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateformat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String变为date的yyyy-MM-dd HH:mm
     */
    public static Date stringToyyyy_MM_dd_HH_mm(String str) {
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date = dateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String变为date的yyyy-MM-dd HH:mm:ss
     */
    public static Date stringToyyyy_MM_dd_HH_mm_ss(String str) {
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = dateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String变为date的yyyy-MM-dd HH:mm:ss
     */
    public static Date stringToyyyy_MM_dd_HH_mm_ss_sss(String str) {
        Date date;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            date = dateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String 变为 date 的EEE
     */

    public static Date stringToEEE(String str) {
        Date date;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("EEE");
            date = dateformat.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date stringToyy_MM(String dateString) {
        Date date;
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM");
            date = dateformat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Date stringtoHH_mm_ss(String dateString) {
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");//大写H为24小时制,小写为12小时
            return dateformat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static String dateToyy_MM_dd_HH_mm_ss(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return dateformat.format(date);
    }

    public static String dateToMM_dd_HH_mm_ss(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm:ss");
        return dateformat.format(date);
    }


    public static String dateToyyyy_MM_dd_HH_mm_ss(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String dateToyyyy_MM_dd_HH_mm_ss_SSS(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(date);
    }

    public static String dateToMM_dd_HH_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH:mm");
        return dateformat.format(date);
    }

    public static String dateToyyyy_MM_dd_HH_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateformat.format(date);
    }

    public static String dateToyyyyMMdd_HHmmss(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateformat.format(date);
    }


    public static String dateToyy_MM_dd_HH_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd HH:mm");
        return dateformat.format(date);
    }


    public static String dateToyy_MM_dd_Enter_HH_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd\n HH:mm");
        return dateformat.format(date);
    }

    /**
     * date类型转换成String yy-MM-dd
     */

    public static String dateToyy_MM_dd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd");
        return dateformat.format(date);
    }


    public static String dateToMM月dd日(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM月dd日");
        return dateformat.format(date);
    }

    public static String dateToMMM月(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM");
        String MM = dateformat.format(date);
        return MMToMMM(MM) + "月";
    }

    public static String dateToMM月(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM月");
        return dateformat.format(date);
    }


    public static String dateToyy(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy");
        return dateformat.format(date);
    }


    public static String dateToyyyy(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
        return dateformat.format(date);
    }


    public static String dateToMM(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM");
        return dateformat.format(date);
    }


    public static String dateToMMPotindd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("MM.dd");
        return dateformat.format(date);
    }


    public static String dateToWeekEEE(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE");
        return dateformat.format(date);
    }

    public static String dateToWeekEEEE(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("EEEE");
        return dateformat.format(date);
    }


    public static String dateToWeekEE(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("EE");
        return dateformat.format(date);
    }


    public static String dateToHH_mm_ss(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
        return dateformat.format(date);
    }


    public static String dateToHH_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
        return dateformat.format(date);
    }

    public static String dateTohh_mm(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("hh:mm");
        return dateformat.format(date);
    }

    /**
     * return  String型   15-07
     */

    public static String dateToyy_MM(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM");
        return dateformat.format(date);
    }


    public static String dateToyy年MM月(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy年MM月");
        return dateformat.format(date);
    }


    public static String dateToyyyy年MM月dd日(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy年MM月dd日");
        return dateformat.format(date);
    }


    public static String dateToyyyy_MM_dd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }

    public static String dateToyyyy_MM(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM");
        return dateformat.format(date);
    }


    public static String dateToyyyy_MMdd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MMdd");
        return dateformat.format(date);
    }


    /**
     * return  String型    31
     */

    public static String dateTodd(Date date) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd");
        return dateformat.format(date);
    }

    /**
     * 将Date型的 yy-MM-dd转换成String型的星期几
     */
    public static String dateyy_MM_ddToEEEE(Date date) {
        int intWeek = date.getDay();
        String strWeek = null;
        if (intWeek == 0) {
            strWeek = "周日";
        }
        if (intWeek == 1) {
            strWeek = "周一";
        }
        if (intWeek == 2) {
            strWeek = "周二";
        }
        if (intWeek == 3) {
            strWeek = "周三";
        }
        if (intWeek == 4) {
            strWeek = "周四";
        }
        if (intWeek == 5) {
            strWeek = "周五";
        }
        if (intWeek == 6) {
            strWeek = "周六";
        }
        return strWeek;
    }

    /**
     * 将01转成一，02转成二
     */
    public static String MMToMMM(String mm) {
        String strWeek = "";
        if (mm.equals("01") || mm.equals("1")) {
            strWeek = "一";
        } else if (mm.equals("02") || mm.equals("2")) {
            strWeek = "二";
        } else if (mm.equals("03") || mm.equals("3")) {
            strWeek = "三";
        } else if (mm.equals("04") || mm.equals("4")) {
            strWeek = "四";
        } else if (mm.equals("05") || mm.equals("5")) {
            strWeek = "五";
        } else if (mm.equals("06") || mm.equals("6")) {
            strWeek = "六";
        } else if (mm.equals("07") || mm.equals("7")) {
            strWeek = "七";
        } else if (mm.equals("08") || mm.equals("8")) {
            strWeek = "八";
        } else if (mm.equals("09") || mm.equals("9")) {
            strWeek = "九";
        } else if (mm.equals("10")) {
            strWeek = "十";
        } else if (mm.equals("11")) {
            strWeek = "十一";
        } else if (mm.equals("12")) {
            strWeek = "十二";
        }
        return strWeek;
    }

    /**
     * 将Long型 毫秒 转换成 String 型 yy-MM-dd HH:mm:ss
     */

    public static String longToDate(Long millisecond) {
        SimpleDateFormat dateformat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return dateformat.format(millisecond);
    }

    /**
     * 将Long型 毫秒 转换成 String 型 yy-MM-dd
     */

    public static String longToyyMMdd(Long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    /**
     * 将整秒转换成 HH:mm:ss的格式，</br>
     * 例如： 130秒 转换 成 02:10
     *
     * @return
     */

    public static String secondTommss(int time) {
        int hour = time / 3600;
        int minute = time / 60;
        int second = time % 60;
        String str_minute = minute + "";
        String str_second = second + "";
        String str_hour = hour + "";
        if (str_minute.length() == 1) {
            str_minute = "0" + str_minute;
        }
        if (str_second.length() == 1) {
            str_second = "0" + str_second;
        }
        if (str_hour.length() == 1) {
            str_hour = "0" + str_hour;
        }
//        return str_hour.equals("00") ? "" : (str_hour + ":") + str_minute + ":" + str_second;
        return str_hour + ":" + str_minute + ":" + str_second;
    }

    /**
     * 把yyyy-MM-dd HH:mm:ss 转化成 时间戳
     */
    public static String yy_MM_dd_HH_mm_ssToTimeStamp(String dataStr) {
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            timeStamp = sdf.parse(dataStr).getTime() + "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * 把yyyy-MM-dd HH:mm:ss 转化成long 时间戳
     */
    public static long yy_MM_dd_HH_mmToLong(String dataStr) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            timeStamp = sdf.parse(dataStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    /**
     * @param year
     * @param month
     * @return
     */
    public static int getDayCount(int year, int month) {
        int dayCount;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 2:
                if (year % 4 == 0) {
                    dayCount = 29;
                } else {
                    dayCount = 28;
                }
                break;
            default:
                dayCount = 30;
                break;
        }
        return dayCount;
    }
}