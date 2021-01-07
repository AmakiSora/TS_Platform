package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/admin/{admin}")
    public String admin(@PathVariable("admin") String admin){
        return "/admin/"+admin;
    }
    @RequestMapping("/admin/students.html")
    public String queryStudentList(Model model){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = userDetails.getUsername();
        List<Student> studentList = TSMapper.queryStudentList();
        model.addAttribute("studentList",studentList);
        return "/admin/students.html";
    }
    @PostMapping("/admin/students.html")//增加学生
    public String addStu(Student student){
//        System.out.println(student);//测试用
        TSMapper.addStudent(student);//增加学生信息进student表
        User user = new User();
        user.setUsername(student.getId().toString());
        user.setPassword("111");//默认密码111
        user.setRole("student");//设置权限为学生
        userMapper.addStu(user);//增加帐号进user表
        return "redirect:/admin/students.html";//重定向到students页面
    }
    @GetMapping("/delete/{id}")//删除学生
    public String deleteStu(@PathVariable("id")int id){
        TSMapper.deleteStudent(id);
        String username = String.valueOf(id);
        userMapper.deleteStu(username);
        return "redirect:/admin/students.html";
    }

}
