package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
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
    private TSMapper TSMapper;
    @RequestMapping("/student/{student}")
    public String student(@PathVariable("student") String student){
        return "/student/"+student;
    }
    @RequestMapping("/student/index.html")//首页
    public String index(Model model){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//获取用户名
//        String username = userDetails.getUsername();
//        String name = TSMapper.queryNameById(username);
//        model.addAttribute("name",name);
        return "/student/index.html";
    }

    @RequestMapping("/student/staff.html")//查询老师信息
    public String queryStaffList(Model model){
        List<Staff> staffList = TSMapper.queryStaffList();
        model.addAttribute("staffList",staffList);
        return "/student/staff.html";
    }

    @RequestMapping("/student/students.html")//查询同班同学的学生信息
    public String queryStudentList(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//获取用户名
        String username = userDetails.getUsername();
        String classes = TSMapper.queryClassesByName(username);
        List<Student> studentList = TSMapper.queryStudentByClasses(classes);
        model.addAttribute("studentList",studentList);
        return "/student/students.html";
    }


}
