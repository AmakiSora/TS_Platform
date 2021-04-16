package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.CampusNews;
import com.cosmos.pojo.Comment;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Task;
import com.cosmos.service.MainService;
import com.cosmos.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    //讨论内容
    @Override
    public void commentList(String position, Model model) {
        List<Comment> commentList = TSMapper.queryCommentList(position);
        for (Comment list:commentList){
            if(!list.getRepliesNum().equals(0)){//如果回复人数不为零
                String a = list.getNO().toString();
                model.addAttribute(a,TSMapper.queryCommentList(a));
            }
        }
        model.addAttribute("comment",commentList);
    }
    //作业列表
    @Override
    public List<Task> queryTaskList(String courseID) {
        List<Task> task = TSMapper.queryTaskList(courseID);
        Date now = new Date();
        for(Task list:task){
            Date i = list.getIssuedDate();
            Date j = list.getDeadline();
            if(now.after(i)&&now.before(j)){//进行中
                list.setState("1");
            }else if(now.after(j)){//已截止
                list.setState("2");
            }else if(now.before(i)){//未开始
                list.setState("3");
            }else{
                list.setState("4");
            }
        }
        return task;
    }
    //获取今日课程表
    /**
     * schedule字段设计
     * 固定数组第一位(schedule[0])为课程周数范围，数组后面为每节课具体时间，以;号分开每节具体课程
     * 规定:
     *      起始周数(1-25),结束周数(1-25);单双周(0全1单2双),星期几(0-7),课程节数(必须连续),说明(实验/理论课,授课地点)
     * 例子:
     *      1,16;1,0,1-2,理论课A教502
     *      1-16周 ; 单周 星期日 第1,2节课 理论课 A教502
     */
    @Override
    public List<Map<String,String>> getTodayCoursesSchedule() {
        String role = session.getAttribute("role").toString();
        if(role.equals("staff")){
            List<Course> courses = TSMapper.queryTeaCourse(session.getAttribute("id").toString());
            return handleSchedule(courses,role);
        }else if(role.equals("student")){
            List<Course> courses = TSMapper.queryStuCourse(session.getAttribute("id").toString());
            return handleSchedule(courses,role);
        }else {
            return null;
        }
    }
    //处理今日课程表
    private List<Map<String, String>> handleSchedule(List<Course> courses,String role) {
        Calendar calendar = Calendar.getInstance();
        if(!courses.isEmpty()){//判断老师是否有课
            List<Map<String,String>> list = new ArrayList<>();
            for(Course course:courses){//循环每种课
                if(course.getSchedule()!=null && !course.getSchedule().equals("")){//schedule字段表不为空
                    String[] a= course.getSchedule().split(";");
                    String[] range = a[0].split(",");
                    int startWeek = Integer.parseInt(range[0]);
                    int endWeek = Integer.parseInt(range[1]);
                    if(DateUtils.currentWeeks.intValue()>=startWeek &&
                            DateUtils.currentWeeks.intValue()<=endWeek){//判断当前周是否在课程周内
                        for (int i = 1; i < a.length; i++) {//循环此课的具体每节课
                            String[] Class= a[i].split(",");
                            int cal = calendar.get(Calendar.DAY_OF_WEEK)-1;
                            if ((Class[0].equals("0") ||
                                    Class[0].equals(DateUtils.oddEvenWeek)) &&
                                    Class[1].equals(Integer.toString(cal))) {//判断单双周,判断星期
                                Map<String,String> map = new HashMap<>();
                                map.put("name",course.getName());
                                map.put("lesson",Class[2]);
                                map.put("message",Class[3]);
                                map.put("link","/"+role+"/courses/"+course.getId());
                                list.add(map);
                            }
                        }
                    }
                }
            }
//            System.out.println(list);
//            System.out.print("今天星期");
//            System.out.println(calendar.get(Calendar.DAY_OF_WEEK)-1);
//            System.out.println("当前周数"+DateUtils.currentWeeks);
//            System.out.println("现在是"+DateUtils.oddEvenWeek+"周");
            if(list.isEmpty()){
                return null;
            }else {
                return list;
            }

        }
        return null;
    }
    //首页校园新闻
    @Override
    public List<CampusNews> getCampusNewsList() {
        return TSMapper.getCampusNewsList();
    }
}
