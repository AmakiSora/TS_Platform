package com.cosmos.controller;

import com.cosmos.TsPlatformApplication;
import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.*;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
        session.setAttribute("teacherID",course.getTeacherID());//为消息通知获取用
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

        //讨论
        List<Comment> commentList = TSMapper.queryCommentList(id);
        for (Comment list:commentList){
            if(!list.getRepliesNum().equals(0)){//如果回复人数不为零
                String a = list.getNO().toString();
                model.addAttribute(a,TSMapper.queryCommentList(a));
            }
        }
        model.addAttribute("comment",commentList);

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
            //查询我的作业
            Map<String,Object> myTask = TSMapper.queryMyTaskFile(session.getAttribute("id").toString(),id);
            if(myTask==null){
                model.addAttribute("xxx",1);
            }else {
                myTask.putIfAbsent("score", "未批改");//如果score为null，则显示“未批改”
                model.addAttribute("myTask",myTask);
            }
            //讨论
            List<Comment> commentList = TSMapper.queryCommentList(id);
            for (Comment list:commentList){
                if(!list.getRepliesNum().equals(0)){//如果回复人数不为零
                    String a = list.getNO().toString();
                    model.addAttribute(a,TSMapper.queryCommentList(a));
                }
            }
            model.addAttribute("comment",commentList);
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
    @ResponseBody
    public int submitTask(@RequestParam Map<String,String> task,@RequestParam("file") MultipartFile file) throws IOException, ParseException {
        Date now = new Date();//当前时间
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now1 = sdf.format(now);
        if(sdf.parse(now1).getTime()<sdf.parse(task.get("issuedDate")).getTime()){//转成long类型比较
            return 3;//作业未发布
        }else if(sdf.parse(now1).getTime()>sdf.parse(task.get("deadline")).getTime()){
            return 2;//作业已截止
        }else if(sdf.parse(now1).getTime()>sdf.parse(task.get("issuedDate")).getTime()&&
                 sdf.parse(now1).getTime()<sdf.parse(task.get("deadline")).getTime()){
            if(task.get("fileName").equals("")){//如果文件名为空
                String taskName = session.getAttribute("taskID").toString()+"-"+
                                  session.getAttribute("id").toString()+"-"+
                                  file.getOriginalFilename();//原始文件名
                file.transferTo(new File("D:/cosmos/tete/taskStudent/"+taskName));
                //将提交时间和文件名存进数据库
                TSMapper.submitTask(now,taskName,session.getAttribute("id").toString(),session.getAttribute("taskID").toString());
            }else {//不为空
                String f = file.getOriginalFilename();//原始文件名
                String taskName = session.getAttribute("taskID").toString()+"-"+
                                  session.getAttribute("id").toString()+"-"+
                                  task.get("fileName")+
                                  f.substring(f.lastIndexOf("."));//后缀名
                file.transferTo(new File("D:/cosmos/tete/taskStudent/"+taskName));
                //将提交时间和文件名存进数据库
                TSMapper.submitTask(now,taskName,session.getAttribute("id").toString(),session.getAttribute("taskID").toString());
            }
            return 1;//提交成功
        }else {
            System.out.println("出错啦，快来修bug");
            return 666;
        }
    }
}
