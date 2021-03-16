package com.cosmos.config;

import com.cosmos.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TimedTask {
    @Scheduled(cron = "0 0 0 ? * *")//每日零点刷新
    public void setCurrentWeeks(){
        DateUtils.currentWeeks = DateUtils.DateInterval(DateUtils.startDay,new Date(),"weeks");//重新计算周数(一天一算)
        DateUtils.oddEvenWeek = DateUtils.JudgmentOfWeek();//重新计算单双周
        System.out.println("当前周数变更");
    }
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void test(){
//        System.out.println("触发2");
//    }
}
