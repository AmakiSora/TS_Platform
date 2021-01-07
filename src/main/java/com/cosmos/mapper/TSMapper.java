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
    //分界线
    List<Student> queryStudentList();//列出学生表
    Student queryStudentById(int id);//根据id搜学生
    List<Student> queryStudentByClasses(String classes);//根据班级列出学生表
    String queryClassesByName(String username);//根据名搜班级
    void addStudent(Student student);//增加学生
    int deleteStudent(int id);//根据id删除学生
    int updateStudent(Student student);//修改学生信息
}
