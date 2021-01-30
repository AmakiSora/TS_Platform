package com.cosmos.mapper;

import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TSMapper {
    List<Staff> queryStaffList();//列出教师表
    Staff queryStaffById(String id);//查询教师(根据id)
    String queryTeaNameById(String id);//搜名字(根据id)
    void addStaff(Staff staff);//增加教师
    void deleteStaff(String id);//删除教师(根据id)
    void updateStaff(Staff staff);//修改教师信息
    //分界线---------------------------------------------------------------------------
    List<Student> queryStudentList();//列出学生表
    Student queryStudentById(String id);//搜学生(根据id)
    String queryStuNameById(String id);//搜名字(根据id)
    List<Student> queryStudentByClasses(String classes);//列出学生表(根据班级)
    String queryClassesByName(String username);//搜班级(根据名)
    void addStudent(Student student);//增加学生
    void deleteStudent(String id);//删除学生(根据id)
    void updateStudent(Student student);//修改学生信息
    //分界线---------------------------------------------------------------------------
    List<Course> queryCourseList();//查询所有课程
    List<Course> queryStuCourse(String id);//查询学生的课程(根据id)
    List<Course> queryTeaCourse(String id);//查询老师的课程(根据id)
    Course queryCourse(String id);//查询课程细节()
    void addCourse(Course course);//开设课程
    List<Student> queryCourseStuList(String id);//查询课程学生名单(根据课程id)
    //分界线---------------------------------------------------------------------------
    List<Task> queryTaskList(String id);//查询课程的所有作业
    Task queryTask(String id);//查询作业详情
    void addTask(Task task);//增加作业
    void updateTask(@Param("task")Task task,@Param("oldID")String oldID);//编辑作业(根据旧id,不改文件)
    void updateTaskF(@Param("task")Task task,@Param("oldID")String oldID);//编辑作业(根据旧id,改变文件)
}
