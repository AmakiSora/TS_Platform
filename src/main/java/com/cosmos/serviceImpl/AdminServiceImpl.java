package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import com.cosmos.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    //列出学生
    @Override
    public List<Student> queryStudentList() {
        return TSMapper.queryStudentList();
    }
    //增加学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addStudent(Student student) {
        TSMapper.addStudent(student);//增加学生信息进student表
        User user = new User();
        user.setUsername(student.getId());
        user.setPassword("111");//默认密码111
        user.setRole("student");//设置权限为学生
        userMapper.addUser(user);//增加帐号进user表
    }
    //删除学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void deleteStudent(String studentID) {
        TSMapper.deleteStudent(studentID);//删除学生表
        userMapper.deleteUser(studentID);//删除用户表
    }
    //更改学生信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void updateStudent(Student student) {
        TSMapper.AdminUpdateStudent(student);
    }
    //重置密码
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void ResetPassword(String id) {
        String Password = "000";//重设的密码
        userMapper.resetPasswordUser(id,Password);
    }
    //列出教师
    @Override
    public List<Staff> queryStaffList() {
        return TSMapper.queryStaffList();
    }

    //增加教师
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addTeacher(Staff staff) {
        TSMapper.addStaff(staff);//增加教师信息进staff表
        User user = new User();
        user.setUsername(staff.getId());
        user.setPassword("222");//默认密码222
        user.setRole("staff");//设置权限为教师
        userMapper.addUser(user);//增加帐号进user表
    }

    //删除教师
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void deleteTeacher(String teacherID) {
        TSMapper.deleteStaff(teacherID);//删除教师表
        userMapper.deleteUser(teacherID);//删除用户表
    }

    //更改教师信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void updateTeacher(Staff staff) {
        TSMapper.AdminUpdateStaff(staff);
    }

    //查询所有课程
    @Override
    public List<Course> queryCourseList() {
        return TSMapper.queryCourseList();
    }

    //增加课程
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addCourse(Course course) {
        TSMapper.addCourse(course);
    }
    //增加课程学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addStudentCourse(String courseID,String studentID) {
            TSMapper.addStudentCourse(courseID,studentID);//加入课程学生表
            List<String> list = TSMapper.queryCourseTaskIDList(courseID);//查询课程的作业id
            if(!list.isEmpty()) {//如果课程里有作业id
                for (String taskID : list) {
                    TSMapper.addTaskStudent(studentID,TSMapper.queryStuNameById(studentID), taskID);//加入作业学生表
                }
            }
            TSMapper.updateCourseStudentNum(courseID);//更新课程的学生人数
    }
}
