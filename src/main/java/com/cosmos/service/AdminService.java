package com.cosmos.service;

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
}
