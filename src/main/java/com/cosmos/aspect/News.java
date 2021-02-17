package com.cosmos.aspect;

import org.aspectj.lang.JoinPoint;
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
    @Pointcut("@annotation(com.cosmos.aspect.NewsAOP)")//切面
    public void Pointcut(){

    }
    @AfterReturning("Pointcut()&&args(position,i,j)")//参数要一一对应
    public void AddNew(JoinPoint joinPoint, String position, String i, HttpServletRequest j){
        String n = joinPoint.getSignature().getName();//获取方法名
        String role = session.getAttribute("role").toString();
        if(role.equals("staff")){//用户为教师
            if(n.equals("discuss")){//发表评论

            }else if(n.equals("reply")){//回复评论
                System.out.println("a2");
            }else if(n.equals("")) {

            }else {
                System.out.println("a3");
            }
        }else if (role.equals("student")){//用户为学生
            if(n.equals("discuss")){//发表评论
                String name = session.getAttribute("name").toString();
                String teaID = session.getAttribute("teacherID").toString();
                if(position.contains("_")){//有下划线为作业
                    redisTemplate.opsForHash().put("news"+teaID,"/staff/task/"+position,name+"同学发表了作业讨论");
                }else {//无则为课程
                    redisTemplate.opsForHash().put("news"+teaID,"/staff/courses/"+position,name+"同学发表了课程评价");
                }
            }
        }else {//管理员

        }

    }
    @AfterReturning("Pointcut()&&args(i,a)")
    public void Asd(String i,String a){
        System.out.println("999");
    }
}
