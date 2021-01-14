package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import com.fasterxml.jackson.databind.DatabindContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class StudentController {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @RequestMapping("/student/{student}")
    public String student(@PathVariable("student") String student){
        return "/student/"+student;
    }

    @RequestMapping("/student/index.html")//首页
    public String index(){
        String name = TSMapper.queryStuNameById(session.getAttribute("id").toString());
        if(session.getAttribute("name")==null||session.getAttribute("name")!=name){//防止重复设置名字
            session.setAttribute("name",name);//名字放进会话
        }
        return "/student/index.html";
    }

    @RequestMapping("/student/staff.html")//查询所有老师信息
    public String queryStaffList(Model model){
        List<Staff> staffList = TSMapper.queryStaffList();
        model.addAttribute("staffList",staffList);
        return "/student/staff.html";
    }

    @RequestMapping("/student/students.html")//查询同班同学的学生信息
    public String queryStudentList(Model model){
        String classes = TSMapper.queryClassesByName((String) session.getAttribute("id"));
        List<Student> studentList = TSMapper.queryStudentByClasses(classes);
        model.addAttribute("studentList",studentList);
        return "/student/students.html";
    }

    @RequestMapping("/student/courses.html")//课程查询
    public String queryCourseList(Model model){
        List<Course> coursesList = TSMapper.queryCourseList();//查询全部课程
        model.addAttribute("coursesList",coursesList);
        List<Course> myCoursesList = TSMapper.queryStuCourse(session.getAttribute("id").toString());//查询学生自己的课程
        model.addAttribute("myCoursesList",myCoursesList);
        return "/student/courses.html";
    }

}
