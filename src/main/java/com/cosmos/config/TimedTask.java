package com.cosmos.config;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Task;
import com.cosmos.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TimedTask {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(TimedTask.class);
    @Scheduled(cron = "0 0 0 ? * *")//每日零点刷新
    public void setCurrentWeeks(){//变更周数
        DateUtils.currentWeeks = DateUtils.DateInterval(DateUtils.startDay,new Date(),"weeks");//重新计算周数(一天一算)
        DateUtils.oddEvenWeek = DateUtils.JudgmentOfWeek();//重新计算单双周
        logger.info("当前周数变更");
    }
    @Scheduled(cron = "0 0 12 * * ?")//每天中午12点触发
    public void taskReminder(){//作业到期提醒
        List<Task> t = TSMapper.getNeedRemindTask();
        if (!t.isEmpty()){
            for (Task task : t) {
                List<String> stus = TSMapper.getUnfinishedTask(task.getId());
                if (!stus.isEmpty()){
                    for (String id : stus) {
                        //todo 这个作业到期通知没有设置过期时间
                        redisTemplate.opsForHash().put("notice"+id,"/notice/task/"+task.getId(),task.getName()+"作业即将截止！");
                    }
                }
            }
        }
        logger.info("作业到期提醒");
    }
}
