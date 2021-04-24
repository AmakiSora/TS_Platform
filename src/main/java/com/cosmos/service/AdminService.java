package com.cosmos.service;

import com.cosmos.pojo.CampusNews;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;

import java.util.List;

public interface AdminService {
    //列出学生
    List<Student> queryStudentList();
    //增加学生
    void addStudent(Student student);
    //删除学生
    void deleteStudent(String studentID);
    //更改学生信息
    void updateStudent(Student student);
    //重置密码
    void ResetPassword(String id);
    //列出教师
    List<Staff> queryStaffList();
    //增加教师
    void addTeacher(Staff staff);
    //删除教师
    void deleteTeacher(String teacherID);
    //更改教师信息
    void updateTeacher(Staff staff);
    //查询所有课程
    List<Course> queryCourseList();
    //增加课程
    void addCourse(Course course);
    //增加课程学生
    void addStudentCourse(String courseID,String studentID);
    //发布校园新闻
    void addCampusNews(CampusNews campusNews);
    //编辑校园新闻
    void editCampusNews(CampusNews campusNews);
    //删除校园新闻
    void deleteCampusNews(Integer id);
}
