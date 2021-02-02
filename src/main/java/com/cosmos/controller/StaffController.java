package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @RequestMapping("/staff/courses.html")//课程列表查询
    public String queryCourseList(Model model){
        List<Course> coursesList = TSMapper.queryCourseList();//查询全部课程
        model.addAttribute("coursesList",coursesList);
        List<Course> myCoursesList = TSMapper.queryTeaCourse(session.getAttribute("id").toString());//查询学生自己的课程
        model.addAttribute("myCoursesList",myCoursesList);
        return "/staff/courses.html";
    }

    @PostMapping("/staff/courses.html")//开设课程
    public String addCourse(Course course){
        TSMapper.addCourse(course);
        return "redirect:/staff/courses.html";
    }

    @RequestMapping("/staff/courses/{id}")//课程详细页面
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
        model.addAttribute("studentList",students);
        return "/staff/courses-details.html";
    }

    @RequestMapping("/staff/task/{id}")//作业详情页
    public String taskDetails(@PathVariable("id")String id, Model model, HttpServletRequest request){
        if(request.getHeader("Referer")==null){//Referer可以获得来源页地址，如果是地址栏输入则值为null
            return "/404";//防止直接从地址栏直接进入此界面
        }else {
            Task task = TSMapper.queryTask(id);//查询作业详情
            session.setAttribute("taskID",id);//为后面增添编辑作业保留旧id
            model.addAttribute("task",task);
            return "/staff/task-details.html";
        }

    }

    @PostMapping("/addTask")//增添作业
    public String addTask(MultipartFile file, Task task)throws IOException {
        task.setCourseID(session.getAttribute("courseID").toString());
        String taskID = session.getAttribute("courseID")+"_"+task.getId();
        task.setId(taskID);
        if(!file.isEmpty()){
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File("D:/cosmos/tete/task/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
            task.setFileName(fileName);//将文件名加入数据库
        }
//        String realPath = session.getServletContext().getRealPath("/static");//获取某目录的实际路径
        TSMapper.addTask(task);

        //下面是添加学生和作业的联系进taskStudent表里
        List<String> studentID = TSMapper.queryCourseStuID(session.getAttribute("courseID").toString());
        if(!studentID.isEmpty()){//如果名单不是空的
            for(String id:studentID){
                System.out.println(id);
                TSMapper.addTaskStudent(id,taskID);
            }
        }

        return "redirect:/staff/task/"+task.getId();
    }

    @PostMapping("/updateTask")//编辑作业
    public String updateTask(MultipartFile file, Task task)throws IOException {
        String oldID = session.getAttribute("taskID").toString();//旧id
        String id= session.getAttribute("courseID").toString()+'_'+task.getId();//新id
        if(!oldID.equals(id)){//如果改变了作业id，作业-学生表也需改变
            TSMapper.changeTaskID(id,oldID);
        }
        task.setId(id);
        if(!file.isEmpty()){//如果有文件更新文件
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File("D:/cosmos/tete/task/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
//            String url = "D:/cosmos/tete/task/"+fileName;
            task.setFileName(fileName);//将文件名加入数据库
            TSMapper.updateTaskF(task,oldID);
        }else {//如果没有文件不更新文件名
            TSMapper.updateTask(task,oldID);
        }
        return "redirect:/staff/task/"+task.getId();
    }

    @RequestMapping("/staff/settings.html")//设置页面
    public String settings(Model model){
        Staff staff = TSMapper.queryStaffById(session.getAttribute("id").toString());
        model.addAttribute("staff",staff);
        return "/staff/settings.html";
    }

    @PostMapping("/staff/settings.html")//修改教师个人信息
    public String setInformation(Staff staff){
        staff.setId(session.getAttribute("id").toString());
        TSMapper.TeaUpdateStaff(staff);
        return "redirect:/staff/settings.html";
    }
}
