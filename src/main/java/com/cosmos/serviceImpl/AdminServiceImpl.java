package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.*;
import com.cosmos.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;//加密
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    //列出学生
    @Override
    public List<Student> queryStudentList() {
        return TSMapper.queryStudentList();
    }
    //增加学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addStudent(Student student) {
        logger.info("增加学生，id为"+student.getId());
        TSMapper.addStudent(student);//增加学生信息进student表
        User user = new User();
        user.setUsername(student.getId());
        user.setPassword(passwordEncoder.encode("111"));//默认密码111
        user.setRole("student");//设置权限为学生
        userMapper.addUser(user);//增加帐号进user表
    }
    //删除学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void deleteStudent(String studentID) {
        logger.info("删除学生，id为"+studentID);
        TSMapper.deleteStudent(studentID);//删除学生表
        userMapper.deleteUser(studentID);//删除用户表
    }
    //更改学生信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void updateStudent(Student student) {
        logger.info("管理员更改学生信息，id为"+student.getId());
        TSMapper.AdminUpdateStudent(student);
    }
    //重置密码
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void ResetPassword(String id) {
        logger.info("重设用户密码，id为"+id);
        String Password = "000";//重设的密码
        userMapper.resetPasswordUser(id,passwordEncoder.encode(Password));
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
        logger.info("增加教师，id为"+staff.getId());
        TSMapper.addStaff(staff);//增加教师信息进staff表
        User user = new User();
        user.setUsername(staff.getId());
        user.setPassword(passwordEncoder.encode("222"));//默认密码222
        user.setRole("staff");//设置权限为教师
        userMapper.addUser(user);//增加帐号进user表
    }

    //删除教师
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void deleteTeacher(String teacherID) {
        logger.info("删除教师，id为"+teacherID);
        TSMapper.deleteStaff(teacherID);//删除教师表
        userMapper.deleteUser(teacherID);//删除用户表
    }

    //更改教师信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void updateTeacher(Staff staff) {
        logger.info("管理员更改教师信息，id为"+staff.getId());
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
        logger.info("管理员增加课程，id为"+course.getId());
        TSMapper.addCourse(course);
    }
    //增加课程学生
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addStudentCourse(String courseID,String studentID) {
        logger.info("增加课程学生，课程id为"+courseID);
        TSMapper.addStudentCourse(courseID,studentID);//加入课程学生表
            List<String> list = TSMapper.queryCourseTaskIDList(courseID);//查询课程的作业id
            if(!list.isEmpty()) {//如果课程里有作业id
                for (String taskID : list) {
                    TSMapper.addTaskStudent(studentID,TSMapper.queryStuNameById(studentID), taskID);//加入作业学生表
                }
            }
            TSMapper.updateCourseStudentNum(courseID);//更新课程的学生人数
    }
    //发布校园新闻
    @Override
    public void addCampusNews(CampusNews campusNews) {
        logger.info("发布校园新闻，id为"+campusNews.getId());
        TSMapper.addCampusNews(campusNews);
    }
    //编辑校园新闻
    @Override
    public void editCampusNews(CampusNews campusNews) {
        logger.info("编辑校园新闻，id为"+campusNews.getId());
        TSMapper.editCampusNews(campusNews);
    }
    //删除校园新闻
    @Override
    public void deleteCampusNews(Integer id) {
        logger.info("删除校园新闻，id为"+id);
        TSMapper.deleteCampusNews(id);
    }
}
