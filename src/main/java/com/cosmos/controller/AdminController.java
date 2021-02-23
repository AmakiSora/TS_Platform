package com.cosmos.controller;

import com.alibaba.excel.EasyExcel;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.service.AdminService;
import com.cosmos.serviceImpl.ExcelListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    private HttpSession session;
    @Autowired
    private AdminService adminService;
    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/{admin}")//默认转发所有
    public String admin(@PathVariable("admin") String admin){
        return "/admin/"+admin;
    }

    @GetMapping("/admin/index.html")//首页
    public String adminIndex(){
        return "/admin/index.html";
    }

    //分割线------------------------------------------------------------------------------------

    @GetMapping("/admin/students.html")//学生页面
    public String queryStudentList(Model model){
        model.addAttribute("studentList",adminService.queryStudentList());
        return "/admin/students.html";

    }

    @PostMapping("/admin/students.html")//增加学生
    public String addStudent(Student student){
        adminService.addStudent(student);
        return "redirect:/admin/students.html";//重定向到students页面
    }

    @GetMapping("/deleteStu/{id}")//删除学生
    public String deleteStu(@PathVariable("id")String id){
        adminService.deleteStudent(id);
        return "redirect:/admin/students.html";
    }

    @ResponseBody
    @PostMapping("/updateStu/{id}")//更改学生信息
    public String updateStu(@RequestBody Student student){
        adminService.updateStudent(student);
        return "1";//需要传值给前端才能执行success：
    }

    @GetMapping("/resetPassword/{id}")//重置密码
    public String ResetPassword(@PathVariable("id")String id){
        adminService.ResetPassword(id);
        return "redirect:/admin/index.html";
    }

    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/staff.html")//列出教师
    public String queryStaffList(Model model) {
        model.addAttribute("staffList", adminService.queryStaffList());
        return "/admin/staff.html";
    }

    @PostMapping("/admin/staff.html")//增加教师
    public String addTea(Staff staff){
        adminService.addTeacher(staff);
        return "redirect:/admin/staff.html";//重定向到staff页面
    }

    @GetMapping("/deleteTea/{id}")//删除教师
    public String deleteTea(@PathVariable("id")String id){
        adminService.deleteTeacher(id);
        return "redirect:/admin/staff.html";
    }

    @ResponseBody
    @PostMapping("/updateTea")//更改教师信息
    public String updateTea(@RequestBody Staff staff){
        adminService.updateTeacher(staff);
        return "1";//需要传值给前端才能执行success：
    }

    //分割线------------------------------------------------------------------------------------

    @GetMapping("/admin/courses.html")//课程页面
    public String courses(Model model){
        model.addAttribute("coursesList",adminService.queryCourseList());//查询全部课程
        return "/admin/courses.html";
    }

    @PostMapping("/admin/courses.html")//增加课程
    public String addCourse(Course course){
        adminService.addCourse(course);
        return "redirect:/admin/courses.html";
    }
    //分割线------------------------------------------------------------------------------------

    @RequestMapping("/admin/settings.html")//设置页面
    public String settings(Model model){
        return "/admin/settings.html";
    }

    @PostMapping("/admin/settings.html")//修改管理员个人信息
    public String setInformation(){

        return "redirect:/admin/settings.html";
    }
    @PostMapping("/uploadExcel/{role}")//上传表格,批量增加
    @ResponseBody
    public String uploadExcel(@PathVariable("role")String role, @RequestParam("file")MultipartFile file) throws IOException {
        switch (role) {
            case "student": //批量增加学生
                EasyExcel.read(file.getInputStream(), Student.class, new ExcelListener(adminService, role)).sheet().doRead();
                break;
            case "staff": //批量增加教师
                EasyExcel.read(file.getInputStream(), Staff.class, new ExcelListener(adminService, role)).sheet().doRead();
                break;
            case "course": //批量增加课程
                EasyExcel.read(file.getInputStream(), Course.class, new ExcelListener(adminService, role)).sheet().doRead();
                break;
            case "studentCourse": //批量增加课程学生
                EasyExcel.read(file.getInputStream(), new ExcelListener(adminService, role)).sheet().doRead();

        }
        return "1";
    }
}
