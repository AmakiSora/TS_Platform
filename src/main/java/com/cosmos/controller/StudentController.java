package com.cosmos.controller;

import com.cosmos.pojo.*;
import com.cosmos.service.MainService;
import com.cosmos.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {
    @Autowired
    private HttpSession session;
    @Autowired
    private StudentService studentService;
    @Autowired
    private MainService mainService;
    @RequestMapping("/student/{student}")//默认转发所有学生页面
    public String student(@PathVariable("student") String student){
        return "/student/"+student;
    }

    @RequestMapping("/student/index.html")//首页
    // TODO: 2021/3/27 学生首页设计
    public String index(){
        String name = studentService.queryStuNameById(session.getAttribute("id").toString());
        if(session.getAttribute("name")==null||session.getAttribute("name")!=name){//防止重复设置名字
            session.setAttribute("name",name);//名字放进会话
        }
        session.setAttribute("schedule",mainService.getTodayCoursesSchedule());//获取今日课程
        return "/student/index.html";
    }

    @GetMapping("/student/staff.html")//教师页面
    public String staff(Model model){
        model.addAttribute("staffList",studentService.queryStaffList());
        return "/student/staff.html";
    }

    @RequestMapping("/student/students.html")//同学页面
    public String students(Model model){
        model.addAttribute("studentList",studentService.queryClassmateList());
        return "/student/students.html";
    }

    @RequestMapping("/student/courses.html")//课程页面
    public String courses(Model model){
        model.addAttribute("coursesList",studentService.queryCourseList());//查询全部课程
        model.addAttribute("myCoursesList",studentService.queryMyCourseList());//查询学生自己的课程
        return "/student/courses.html";
    }

    @RequestMapping("/student/courses/{id}")//课程详细页面
    public String courseDetail(@PathVariable("id")String id,Model model){
        Course course = studentService.courseDetails(id);
        model.addAttribute("detail",course);//详情
        session.setAttribute("courseID",id);//为后面增添作业设置课程id
        session.setAttribute("teacherID",course.getTeacherID());//为消息通知获取用
        model.addAttribute("taskList",mainService.queryTaskList(id));

        List<Student> students = studentService.queryCourseStuList(id);//课程学生列表
        for (Student list:students){//此循环判断学生是否是该课学生
            String a = list.getId();
            if(a.contains(session.getAttribute("id").toString())){
                model.addAttribute("state",1);//是该课学生可以进入作业详情页
            }
        }
        model.addAttribute("studentList",students);

        mainService.commentList(id,model);//讨论
        return "/student/courses-details.html";
    }

    @RequestMapping("/student/task/{id}")//作业详情页
    public String taskDetails(@PathVariable("id")String id,Model model,HttpServletRequest request){
        if(request.getHeader("Referer")==null){//Referer可以获得来源页地址，如果是地址栏输入则值为null
            return "/404";//防止直接从地址栏直接进入此界面
        }else {
            session.setAttribute("taskID",id);//保存作业id
            model.addAttribute("task",studentService.queryTask(id));//查询作业详情
            //查询我的作业
            studentService.queryMyTask(id,model);

            mainService.commentList(id,model);//讨论
            return "/student/task-details.html";
        }
    }
    @GetMapping("/student/chat.html")//聊天页面
    public String chat(Model model){
        model.addAttribute("studentList",studentService.queryClassmateList());
        model.addAttribute("staffList",studentService.queryStaffList());
        return "/student/chat.html";
    }

    @RequestMapping("/student/settings.html")//设置页面
    public String settings(Model model){
        model.addAttribute("student",studentService.queryStudentById(session.getAttribute("id").toString()));
        return "/student/settings.html";
    }

    @PostMapping("/student/settings.html")//修改学生个人信息
    public String setInformation(Student student){
        studentService.StuUpdateStudent(student);
        return "redirect:/student/settings.html";
    }

    @PostMapping("/submitTask")//提交作业
    @ResponseBody
    public int submitTask(@RequestParam Map<String,String> task,@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        return studentService.submitTask(task,file);
    }
}
