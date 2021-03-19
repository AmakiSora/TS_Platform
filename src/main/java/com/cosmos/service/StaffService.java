package com.cosmos.service;

import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface StaffService {
    //查询教师名字(根据id)
    String queryTeaNameById(String studentID);
    //查询所有职工列表
    List<Staff> queryStaffList();
    //列出学生
    List<Student> queryStudentList();
    //查询全部课程
    List<Course> queryCourseList();
    //查询教师自己的课程
    List<Course> queryTeaCourse(String teacherID);
    //开设课程
    void addCourse(Course course);
    //课程详情
    Course queryCourse(String courseID);
    //课程学生列表
    List<Student> queryCourseStuList(String courseID);
    //查询作业详情
    Task queryTask(String taskID);
    //学生作业情况
    List<Map<String,Object>> queryStuTask(String taskID);
    //增添作业
    void addTask(MultipartFile file, Task task) throws IOException;
    //编辑作业
    void updateTask(MultipartFile file, Task task) throws IOException;
    //批改作业
    void taskCorrecting(String id, Integer score);
    //查询教师个人信息
    Staff queryStaffById(String teacherID);
    //修改教师个人信息
    void setInformation(Staff staff);
}
