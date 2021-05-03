package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import com.cosmos.service.StaffService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Value("${fileURI}")
    private String fileURI;
    //查询教师名字(根据id)
    @Override
    public String queryTeaNameById(String studentID) {
        return TSMapper.queryTeaNameById(studentID);
    }
    //查询所有职工列表
    @Override
    public List<Staff> queryStaffList() {
        return TSMapper.queryStaffList();
    }
    //列出学生
    @Override
    public List<Student> queryStudentList() {
        return TSMapper.queryStudentList();
    }
    //查询全部课程
    @Override
    public List<Course> queryCourseList() {
        return TSMapper.queryCourseList();
    }
    //查询教师自己的课程
    @Override
    public List<Course> queryTeaCourse(String teacherID) {
        return TSMapper.queryTeaCourse(teacherID);
    }
    //开设课程
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addCourse(Course course) {
        course.setTeacher(session.getAttribute("name").toString());
        course.setTeacherID(session.getAttribute("id").toString());
        TSMapper.addCourse(course);
    }
    //课程详情
    @Override
    public Course queryCourse(String courseID) {
        return TSMapper.queryCourse(courseID);
    }
    //课程学生列表
    @Override
    public List<Student> queryCourseStuList(String courseID) {
        return TSMapper.queryCourseStuList(courseID);
    }
    //查询作业详情
    @Override
    public Task queryTask(String taskID) {
        return TSMapper.queryTask(taskID);
    }
    //学生作业情况
    @Override
    public List<Map<String, Object>> queryStuTask(String taskID) {
        List<Map<String,Object>> StuFileList = TSMapper.queryStuTaskFileList(taskID);
        for (Map<String,Object> list:StuFileList){
            list.putIfAbsent("fileName", "未提交");
            list.putIfAbsent("score", "未批改");
            list.putIfAbsent("submitDate",null);
        }
        return StuFileList;
    }
    //增添作业
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void addTask(MultipartFile file, Task task) throws IOException {
        task.setCourseID(session.getAttribute("courseID").toString());
        String taskID = session.getAttribute("courseID")+"_"+task.getId();
        task.setId(taskID);
        if(!file.isEmpty()){
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File(fileURI+"task/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
            task.setFileName(fileName);//将文件名加入数据库
        }
        //String realPath = session.getServletContext().getRealPath("/static");//获取某目录的实际路径
        TSMapper.addTask(task);
        //下面是添加学生和作业的联系进taskStudent表里
        List<String> studentID = TSMapper.queryCourseStuID(session.getAttribute("courseID").toString());
        if(!studentID.isEmpty()){//如果名单不是空的
            for(String id:studentID){
                TSMapper.addTaskStudent(id,TSMapper.queryStuNameById(id),taskID);
            }
        }
    }
    //编辑作业
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void updateTask(MultipartFile file, Task task) throws IOException {
        String oldID = session.getAttribute("taskID").toString();//旧id
        String id= session.getAttribute("courseID").toString()+'_'+task.getId();//新id
        if(!oldID.equals(id)){//如果改变了作业id，作业-学生表也需改变
            TSMapper.changeTaskID(id,oldID);
        }
        task.setId(id);
        if(!file.isEmpty()){//如果有文件更新文件
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File(fileURI+"task/"+fileName));
//            String url = "D:/cosmos/tete/task/"+fileName;
            task.setFileName(fileName);//将文件名加入数据库
            TSMapper.updateTaskF(task,oldID);
        }else {//如果没有文件不更新文件名
            TSMapper.updateTask(task,oldID);
        }
    }
    //批改作业
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void taskCorrecting(String id, Integer score) {
        TSMapper.taskCorrecting(id,session.getAttribute("taskID").toString(),score);
    }
    //查询教师个人信息
    @Override
    public Staff queryStaffById(String teacherID) {
        return TSMapper.queryStaffById(teacherID);
    }
    //修改教师个人信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void setInformation(Staff staff) {
        staff.setId(session.getAttribute("id").toString());//防止乱改
        TSMapper.TeaUpdateStaff(staff);
    }
}
