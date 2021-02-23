package com.cosmos.controller;

import com.cosmos.pojo.*;
import com.cosmos.service.MainService;
import com.cosmos.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class StaffController {
    @Autowired
    private HttpSession session;
    @RequestMapping("/staff/{staff}")
    public String staff(@PathVariable("staff") String staff){
        return "/staff/"+staff;
    }
    @Autowired
    private MainService mainService;
    @Autowired
    private StaffService staffService;
    @GetMapping("/staff/index.html")//首页
    public String index(){
        String name = staffService.queryTeaNameById(session.getAttribute("id").toString());
        if(session.getAttribute("name")==null||session.getAttribute("name")!=name){//防止重复设置名字
            session.setAttribute("name",name);//名字放进会话
        }
        return "/staff/index.html";
    }

    @GetMapping("/staff/staff.html") //职工页面
    public String staff(Model model){
        model.addAttribute("staffList",staffService.queryStaffList());
        return "staff/staff.html";
    }

    @GetMapping("/staff/students.html")//学生页面
    public String students(Model model){
        model.addAttribute("studentList",staffService.queryStudentList());
        return "/staff/students.html";
    }

    @GetMapping("/staff/courses.html")//课程页面
    public String courses(Model model){
        model.addAttribute("coursesList",staffService.queryCourseList());//查询全部课程
        model.addAttribute("myCoursesList",staffService.queryTeaCourse(session.getAttribute("id").toString()));//查询教师自己的课程
        return "/staff/courses.html";
    }

    @PostMapping("/staff/courses.html")//开设课程
    public String addCourse(Course course){
        staffService.addCourse(course);
        return "redirect:/staff/courses.html";
    }

    @GetMapping("/staff/courses/{id}")//课程详细页面
    public String coursesDetails(@PathVariable("id")String id,Model model){
        Course course = staffService.queryCourse(id);
        model.addAttribute("detail",course);//详情
        session.setAttribute("courseID",id);//为后面增添作业设置课程id
        session.setAttribute("teacherID",course.getTeacherID());//为消息通知获取ID用
        model.addAttribute("taskList",mainService.queryTaskList(id));//作业
        model.addAttribute("studentList",staffService.queryCourseStuList(id));//课程学生列表
        mainService.commentList(id,model);//讨论
        return "/staff/courses-details.html";
    }

    @GetMapping("/staff/task/{id}")//作业详情页
    public String taskDetails(@PathVariable("id")String id, Model model, HttpServletRequest request){
        if(request.getHeader("Referer")==null){//Referer可以获得来源页地址，如果是地址栏输入则值为null
            return "/404";//防止直接从地址栏直接进入此界面
        }else {
            session.setAttribute("taskID",id);//为后面增添编辑作业保留旧id
            model.addAttribute("task",staffService.queryTask(id));//查询作业详情
            model.addAttribute("StuFileList",staffService.queryStuTask(id));//学生作业情况
            mainService.commentList(id,model);//讨论
            return "/staff/task-details.html";
        }

    }

    @PostMapping("/addTask")//增添作业
    public String addTask(MultipartFile file, Task task)throws IOException {
        staffService.addTask(file,task);
        return "redirect:/staff/task/"+task.getId();
    }

    @PostMapping("/updateTask")//编辑作业
    public String updateTask(MultipartFile file, Task task)throws IOException {
        staffService.updateTask(file, task);
        return "redirect:/staff/task/"+task.getId();
    }

    @ResponseBody
    @PostMapping("/staff/taskCorrecting/{id}")//批改作业
    public String taskCorrecting(@PathVariable("id")String id,Integer score){
        staffService.taskCorrecting(id, score);
        return "1";
    }

    @GetMapping("/staff/settings.html")//设置页面
    public String settings(Model model){
        model.addAttribute("staff",staffService.queryStaffById(session.getAttribute("id").toString()));
        return "/staff/settings.html";
    }

    @PostMapping("/staff/settings.html")//修改教师个人信息
    public String setInformation(Staff staff){
        staffService.setInformation(staff);
        return "redirect:/staff/settings.html";
    }
}
