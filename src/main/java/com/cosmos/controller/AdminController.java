package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/{admin}")//默认转发所有
    public String admin(@PathVariable("admin") String admin){
        return "/admin/"+admin;
    }

    @RequestMapping("/admin/index.html")//首页
    public String adminIndex(){
        return "/admin/index.html";
    }

    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/students.html")//列出学生
    public String queryStudentList(Model model){
        List<Student> studentList = TSMapper.queryStudentList();
        model.addAttribute("studentList",studentList);
        return "/admin/students.html";

    }

    @PostMapping("/admin/students.html")//增加学生
    public String addStu(Student student){
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
        TSMapper.AdminUpdateStudent(student);
        return "1";//需要传值给前端才能执行success：
    }

    @RequestMapping("/resetPassword/{id}")//重置密码
    public String ResetPassword(@PathVariable("id")String id){
        String Password = "000";//重设的密码
        userMapper.resetPasswordUser(id,Password);
        return "redirect:/admin/index.html";
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
    @PostMapping("/updateTea")//更改教师信息
    public String updateTea(@RequestBody Staff staff){
        TSMapper.AdminUpdateStaff(staff);
        return "1";//需要传值给前端才能执行success：
    }

    @RequestMapping("/admin/settings.html")//设置页面
    public String settings(Model model){

        return "/admin/settings.html";
    }

    @PostMapping("/admin/settings.html")//修改管理员个人信息
    public String setInformation(){

        return "redirect:/admin/settings.html";
    }
}
