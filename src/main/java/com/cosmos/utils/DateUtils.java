package com.cosmos.utils;

import java.util.Date;

public class DateUtils {//日期工具类
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
