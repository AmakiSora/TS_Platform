package com.cosmos.serviceImpl;

import com.cosmos.mapper.UserMapper;
import com.cosmos.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    //消息通知
    @Override
    public Map notice(String id) {
        String key="notice"+id;
        long i = redisTemplate.opsForHash().size(key);
        if(i == 0){//没有通知
            return null;
        }else {//有通知
            Map map = redisTemplate.opsForHash().entries(key);
            return map;
        }
    }
    //清空通知
    @Override
    public Integer noticeClear(String id) {
        redisTemplate.delete("notice"+id);
        return 1;
    }
    //消息重定向x
    @Override
    public String noticeReturn(String X,String id) {
        String role = userMapper.queryRoleByName(id);
        if ("student".equals(role)){
            return "redirect:/student/"+X;
        }else if ("staff".equals(role)){
            return "redirect:/staff/"+X;
        }else {
            return null;
        }
    }
    //消息重定向xy
    @Override
    public String noticeReturn(String X, String Y,String id) {
        String role = userMapper.queryRoleByName(id);
        System.out.println(X+"/"+Y);
        if ("student".equals(role)){
            return "redirect:/student/"+X+"/"+Y;
        }else if ("staff".equals(role)){
            return "redirect:/staff/"+X+"/"+Y;
        }else {
            return null;
        }
    }
}
