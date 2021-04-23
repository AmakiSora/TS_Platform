package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import com.cosmos.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    //查询学生名字(根据id)
    @Override
    public String queryStuNameById(String studentID) {
        return TSMapper.queryStuNameById(studentID);
    }
    //查询所有老师信息
    @Override
    public List<Staff> queryStaffList(){
        return TSMapper.queryStaffList();
    }
    //查询同班同学的学生信息
    @Override
    public List<Student> queryClassmateList(){
        String classes = TSMapper.queryClassesByName((String) session.getAttribute("id"));
        return TSMapper.queryStudentByClasses(classes);
    }
    //查询全部课程
    @Override
    public List<Course> queryCourseList(){
        return TSMapper.queryCourseList();
    }
    //查询学生自己的课程
    @Override
    public List<Course> queryMyCourseList(){
        return TSMapper.queryStuCourse(session.getAttribute("id").toString());
    }
    //课程详细
    @Override
    public Course courseDetails(String courseID) {
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
    //查询我的作业
    @Override
    public void queryMyTask(String taskID,Model model) {
        Map<String,Object> myTask = TSMapper.queryMyTaskFile(session.getAttribute("id").toString(),taskID);
        if(myTask==null){
            model.addAttribute("xxx",1);
        }else {
            myTask.putIfAbsent("score", "未批改");//如果score为null，则显示“未批改”
            myTask.putIfAbsent("fileName","无文件");
            model.addAttribute("myTask",myTask);
        }
    }
    //提交作业
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public int submitTask(Map<String, String> task, MultipartFile file) throws ParseException, IOException {
        Date now = new Date();//当前时间
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now1 = sdf.format(now);
        if(sdf.parse(now1).getTime()<sdf.parse(task.get("issuedDate")).getTime()){//转成long类型比较
            return 3;//作业未发布
        }else if(sdf.parse(now1).getTime()>sdf.parse(task.get("deadline")).getTime()){
            return 2;//作业已截止
        }else if(sdf.parse(now1).getTime()>sdf.parse(task.get("issuedDate")).getTime()&&
                sdf.parse(now1).getTime()<sdf.parse(task.get("deadline")).getTime()){
            if(task.get("fileName").equals("")){//如果文件名为空
                String taskName = session.getAttribute("taskID").toString()+"-"+
                        session.getAttribute("id").toString()+"-"+
                        file.getOriginalFilename();//原始文件名
                // TODO: 2021/4/23 文件路径改为通过配置文件获取 
                file.transferTo(new File("D:/cosmos/tete/taskStudent/"+taskName));
                //将提交时间和文件名存进数据库
                TSMapper.submitTask(now,taskName,session.getAttribute("id").toString(),session.getAttribute("taskID").toString());
            }else {//不为空
                String f = file.getOriginalFilename();//原始文件名
                String taskName = session.getAttribute("taskID").toString()+"-"+
                        session.getAttribute("id").toString()+"-"+
                        task.get("fileName")+
                        f.substring(f.lastIndexOf("."));//后缀名
                file.transferTo(new File("D:/cosmos/tete/taskStudent/"+taskName));
                //将提交时间和文件名存进数据库
                TSMapper.submitTask(now,taskName,session.getAttribute("id").toString(),session.getAttribute("taskID").toString());
            }
            return 1;//提交成功
        }else {
            System.out.println("出错啦，快来修bug");
            return 666;
        }
    }
    //查询学生个人信息
    @Override
    public Student queryStudentById(String studentID) {
        return TSMapper.queryStudentById(studentID);
    }
    //修改学生个人信息
    @Override
    @Transactional(rollbackFor = Exception.class)//事务声明，如果报错则回滚
    public void StuUpdateStudent(Student student) {
        student.setId(session.getAttribute("id").toString());//防止乱改
        TSMapper.StuUpdateStudent(student);
    }
}
