package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class StaffController {
    @Autowired
    private HttpSession session;
    @RequestMapping("/staff/{staff}")
    public String staff(@PathVariable("staff") String staff){
        return "/staff/"+staff;
    }
    @Autowired
    private TSMapper TSMapper;

    @RequestMapping("/staff/index.html")//首页
    public String index(){
        String name = TSMapper.queryTeaNameById(session.getAttribute("id").toString());
        if(session.getAttribute("name")==null||session.getAttribute("name")!=name){//防止重复设置名字
            session.setAttribute("name",name);//名字放进会话
        }
        return "/staff/index.html";
    }
    @RequestMapping("/staff/staff.html") //查询所有职工列表
    public String queryStaffList(Model model){
        List<Staff> staffList = TSMapper.queryStaffList();
        model.addAttribute("staffList",staffList);
        return "staff/staff.html";
    }
    @RequestMapping("/staff/courses.html")//课程查询
    public String queryCourseList(Model model){
        List<Course> coursesList = TSMapper.queryCourseList();//查询全部课程
        model.addAttribute("coursesList",coursesList);
        List<Course> myCoursesList = TSMapper.queryTeaCourse(session.getAttribute("id").toString());//查询学生自己的课程
        model.addAttribute("myCoursesList",myCoursesList);
        return "/staff/courses.html";
    }

    @PostMapping("/staff/courses.html")//开设课程
    public String addCourse(Course course){
        TSMapper.addCourse(course);
        return "redirect:/staff/courses.html";
    }
    @RequestMapping("/staff/courses/{id}")//课程详细页面
    public String coursesDetails(@PathVariable("id")String id,Model model){
        Course course = TSMapper.queryCourse(id);//详情
        model.addAttribute("detail",course);

        List<Task> task = TSMapper.queryTaskList(id);//作业
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
        model.addAttribute("taskList",task);

        List<Student> students = TSMapper.queryCourseStuList(id);//课程学生列表
        model.addAttribute("studentList",students);
        return "/staff/courses-details.html";
    }
}
