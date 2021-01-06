package com.cosmos.mapper;

import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TSMapper {
    List<Staff> queryStaffList();
    Staff queryStaffById(int id);
    List<Student> queryStudentList();
    Student queryStudentById(int id);
    List<Student> queryStudentByClasses(String classes);
    String queryClassesByName(String username);


}
