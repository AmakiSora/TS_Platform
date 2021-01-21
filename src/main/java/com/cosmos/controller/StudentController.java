package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
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
    @RequestMapping("/student/{student}")
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
        return "/student/courses-details.html";
    }

//    @PostMapping("/setAvatar")//学生上传头像
//    public String setAvatar(@RequestParam("file") MultipartFile file) throws IOException {
//        if(file.isEmpty()){
//            return null;
//        }
////        String fileName = System.currentTimeMillis()+file.getOriginalFilename();//根据时间戳产生新的文件名
////        String fileName = session.getAttribute("name").toString();
//        byte[] bytes=file.getBytes();//将图片转换成二进制流
//        Student student = new Student();
//        student.setId(session.getAttribute("id").toString());
//        student.setAvatar(bytes);
////        String path=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/images/download/"+fileName;
//        TSMapper.setStuAvatar(student);
//        return "/student/settings.html";
//    }
//    @RequestMapping("/stuAvatar/{id}")
//    public ResponseEntity<byte[]> stuAvatar (){
//        Student student = TSMapper.queryStudentById();
//        byte[] s = student.getAvatar();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<byte[]>(s,headers, HttpStatus.OK);
//    }
//    @RequestMapping("/student/settings.html")//设置界面
//    public String getData(Model model) throws IOException {
//        Student student = TSMapper.queryStudentById(session.getAttribute("id").toString());
//        if(student.getAvatar()!=null){
//            byte[] d=student.getAvatar();//将图片转换成二进制流
//            ResponseEntity<byte[]> b = ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(student.getAvatar());
////            byte[] bytes = student.getAvatar();
////            System.out.println(bytes);
////            ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);//将二进制字节数组 转为文件
////            Files.copy(inputStream, Paths.get("/avatar/11.jpg"));
//            model.addAttribute("b",b);
//            model.addAttribute("d",d);
//        }
//        return "/student/settings.html";
//    }

}
