package com.cosmos.aspect;

import com.cosmos.mapper.UserMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class News {
    @Autowired
    private HttpSession session;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Pointcut("@annotation(com.cosmos.aspect.NewsAOP.comment)")//评论切面
    public void comment(){}
    @AfterReturning("comment()&&args(position,text,request)")//发表评论,参数要一一对应
    public void discussNews(String position,String text,HttpServletRequest request){
        String role = session.getAttribute("role").toString();
        if(role.equals("staff")){//用户为教师

        }else if (role.equals("student")){//用户为学生
            String name = session.getAttribute("name").toString();
            String teaID = session.getAttribute("teacherID").toString();
            if(position.contains("_")){//有下划线为作业
                redisTemplate.opsForHash().put("news"+teaID,"/staff/task/"+position,name+"同学发表了作业讨论");
            }else {//无则为课程
                redisTemplate.opsForHash().put("news"+teaID,"/staff/courses/"+position,name+"同学发表了课程评价");
            }
        }else {//管理员
            System.out.println("管理员");
        }
    }
    @AfterReturning("comment()&&args(position,text,replier,replierID,request)")//回复评论
    public void replyNews(String position,String text,String replier,String replierID,HttpServletRequest request){
        String name = session.getAttribute("name").toString();
        String url = request.getHeader("Referer");
        if(url.contains("courses")){//课程
            String courseID = session.getAttribute("courseID").toString();
            redisTemplate.opsForHash().put("news"+replierID,"/news/courses/"+courseID,name+"回复了你");
        }else if(url.contains("task")){//作业
            String taskID = session.getAttribute("taskID").toString();
            redisTemplate.opsForHash().put("news"+replierID,"/news/task/"+taskID,name+"回复了你");
        }
    }
// TODO: 2021/4/9 作业到期提醒，教师发布通知

//    @Pointcut("@annotation(com.cosmos.aspect.NewsAOP.chat)")//聊天切面（弃用）
//    public void chat(){}
//    @AfterReturning("chat()&&args(id,name)")
//    public void chatNews(String id,String name){
//        System.out.println(id);
//        System.out.println(name);
//        System.out.println("离线聊天");
//    }

}
