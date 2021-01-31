package com.cosmos.controller;

import com.cosmos.TsPlatformApplication;
import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class StudentController {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @RequestMapping("/student/{student}")//默认转发所有学生页面
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

    @RequestMapping("/student/courses/{id}")//课程详细页面
    public String coursesDetails(@PathVariable("id")String id,Model model){
        Course course = TSMapper.queryCourse(id);//详情
        model.addAttribute("detail",course);

        List<Task> task = TSMapper.queryTaskList(id);//作业
        session.setAttribute("courseID",id);//为后面增添作业设置课程id
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
        for (Student list:students){//此循环判断学生是否是该课学生
            String a = list.getId();
            if(a.contains(session.getAttribute("id").toString())){
                model.addAttribute("state",1);//是该课学生可以进入作业详情页
            }
        }
        model.addAttribute("studentList",students);
        return "/student/courses-details.html";
    }

    @RequestMapping("/student/task/{id}")//作业详情页
    public String taskDetails(@PathVariable("id")String id,Model model,HttpServletRequest request){
        if(request.getHeader("Referer")==null){//Referer可以获得来源页地址，如果是地址栏输入则值为null
            return "/404";//防止直接从地址栏直接进入此界面
        }else {
            Task task = TSMapper.queryTask(id);//查询作业详情
            session.setAttribute("taskID",id);//保存作业id
            model.addAttribute("task",task);
            return "/student/task-details.html";
        }
    }

    @RequestMapping("/student/settings.html")//设置页面
    public String settings(Model model){
        Student student = TSMapper.queryStudentById(session.getAttribute("id").toString());
        model.addAttribute("student",student);
        return "/student/settings.html";
    }

    @PostMapping("/student/settings.html")//修改学生个人信息
    public String setInformation(Student student){
        student.setId(session.getAttribute("id").toString());
        TSMapper.StuUpdateStudent(student);
        return "redirect:/student/settings.html";
    }

    @PostMapping("/submitTask")//提交作业
    public String submitTask(MultipartFile file, Task task)throws IOException {
        task.setCourseID(session.getAttribute("courseID").toString());
        String taskID = session.getAttribute("courseID")+"_"+task.getId();
        task.setId(taskID);
        if(!file.isEmpty()){
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File("D:/cosmos/tete/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
//            String url = "D:/cosmos/tete/"+fileName;
            task.setFileName(fileName);//将文件名加入数据库
        }
//        String realPath = session.getServletContext().getRealPath("/static");//获取某目录的实际路径
        TSMapper.addTask(task);
        return "redirect:/staff/task/"+task.getId();
    }
}
