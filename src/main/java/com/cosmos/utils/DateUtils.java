package com.cosmos.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {//日期工具类
    public static Date startDay = new Date(121, Calendar.JANUARY,1, 0, 0, 0);//2021年1月1日
    public static Long currentWeeks= DateInterval(startDay,new Date(),"weeks");//当前周数
    public static String oddEvenWeek = JudgmentOfWeek();//单双周(1为单，2为双)
    public static String JudgmentOfWeek(){//判断单双周
        if((currentWeeks.intValue() & 1)==1){
            return "1";
        }else {
            return "2";
        }
    }
    public static Long DateInterval(Date a, Date b, String returnType){//日期间隔天数
        long Maxtime = Math.max(a.getTime(), b.getTime());
        long Mintime = Math.min(a.getTime(), b.getTime());
        long l = Maxtime - Mintime;
        if ("days".equals(returnType)) {
            return l/(1000*60*60*24);
        }else if ("weeks".equals(returnType)){
            return l/(1000*60*60*24*7);
        }else if ("months".equals(returnType)){
            return l/(1000L *60*60*24*30);
        }else if ("years".equals(returnType)){
            return l/(1000L *60*60*24*365);
        }else {
            return l;
        }
    }
}
