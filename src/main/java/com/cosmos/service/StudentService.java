package com.cosmos.service;

import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface StudentService {//学生服务
    //查询学生名字(根据id)
    String queryStuNameById(String studentID);
    //查询所有老师信息
    List<Staff> queryStaffList();
    //查询同班同学的学生信息
    List<Student> queryClassmateList();
    //查询全部课程
    List<Course> queryCourseList();
    //查询学生自己的课程
    List<Course> queryMyCourseList();
    //课程详细
    Course courseDetails(String courseID);
    //课程学生列表
    List<Student> queryCourseStuList(String courseID);
    //查询作业详情
    Task queryTask(String taskID);
    //查询我的作业
    void queryMyTask(String taskID, Model model);
    //提交作业
    int submitTask(Map<String,String> task,MultipartFile file) throws ParseException, IOException;
    //查询学生个人信息
    Student queryStudentById(String studentID);
    //修改学生个人信息
    void StuUpdateStudent(Student student);
}
