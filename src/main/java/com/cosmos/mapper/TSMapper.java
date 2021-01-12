package com.cosmos.mapper;

import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TSMapper {
    List<Staff> queryStaffList();//列出教师表
    Staff queryStaffById(int id);//根据id查询教师
    String queryTeaNameById(String id);//根据id搜名字
    void addStaff(Staff staff);//增加教师
    void deleteStaff(String id);//根据id删除教师
    void updateStaff(Staff staff);//修改教师信息
    //分界线
    List<Student> queryStudentList();//列出学生表
    Student queryStudentById(String id);//根据id搜学生
    String queryStuNameById(String id);//根据id搜名字
    List<Student> queryStudentByClasses(String classes);//根据班级列出学生表
    String queryClassesByName(String username);//根据名搜班级
    void addStudent(Student student);//增加学生
    void deleteStudent(String id);//根据id删除学生
    void updateStudent(Student student);//修改学生信息

}
