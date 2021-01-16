package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
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
        List<Course> myCoursesList = TSMapper.queryTeaCourse(session.getAttribute("name").toString());//查询学生自己的课程
        model.addAttribute("myCoursesList",myCoursesList);
        return "/staff/courses.html";
    }

    @PostMapping("/staff/courses.html")//开设课程
    public String addCourses(Course course){
        TSMapper.addCourse(course);
        return "redirect:/staff/courses.html";
    }
}
