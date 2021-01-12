package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/admin/{admin}")//默认转发所有
    public String admin(@PathVariable("admin") String admin){
        return "/admin/"+admin;
    }

    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/students.html")//列出学生
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
        user.setUsername(student.getId());
        user.setPassword("111");//默认密码111
        user.setRole("student");//设置权限为学生
        userMapper.addUser(user);//增加帐号进user表
        return "redirect:/admin/students.html";//重定向到students页面
    }
    @GetMapping("/deleteStu/{id}")//删除学生
    public String deleteStu(@PathVariable("id")String id){
        TSMapper.deleteStudent(id);//删除学生表
        String username = id;
        userMapper.deleteUser(username);//删除用户表
        return "redirect:/admin/students.html";
    }
    @ResponseBody
    @PostMapping("/updateStu/{id}")//更改学生信息
    public String updateStu(@RequestBody Student student){
//        System.out.println(student.getId());
        TSMapper.updateStudent(student);
        return "1";//需要传值给前端才能执行success：
    }

    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/staff.html")//列出教师
    public String queryStaffList(Model model) {
        List<Staff> staffList = TSMapper.queryStaffList();
        model.addAttribute("staffList", staffList);
        return "/admin/staff.html";
    }
    @PostMapping("/admin/staff.html")//增加教师
    public String addTea(Staff staff){
//        System.out.println(staff);//测试用
        TSMapper.addStaff(staff);//增加教师信息进staff表
        User user = new User();
        user.setUsername(staff.getId());
        user.setPassword("222");//默认密码222
        user.setRole("staff");//设置权限为教师
        userMapper.addUser(user);//增加帐号进user表
        return "redirect:/admin/staff.html";//重定向到staff页面
    }
    @GetMapping("/deleteTea/{id}")//删除教师
    public String deleteTea(@PathVariable("id")String id){
        TSMapper.deleteStaff(id);//删除教师表
        String username = id;
        userMapper.deleteUser(username);//删除用户表
        return "redirect:/admin/staff.html";
    }
    @ResponseBody
    @PostMapping("/updateTea/{id}")//更改教师信息
    public String updateTea(@RequestBody Staff staff){
//        System.out.println(staff.getId());
        TSMapper.updateStaff(staff);
        return "1";//需要传值给前端才能执行success：
    }
}
