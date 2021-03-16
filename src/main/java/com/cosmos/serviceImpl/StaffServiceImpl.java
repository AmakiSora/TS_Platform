package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.Task;
import com.cosmos.service.StaffService;
import com.cosmos.utils.DateUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
    public void addTask(MultipartFile file, Task task) throws IOException {
        task.setCourseID(session.getAttribute("courseID").toString());
        String taskID = session.getAttribute("courseID")+"_"+task.getId();
        task.setId(taskID);
        if(!file.isEmpty()){
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File("D:/cosmos/tete/task/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
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
    public void updateTask(MultipartFile file, Task task) throws IOException {
        String oldID = session.getAttribute("taskID").toString();//旧id
        String id= session.getAttribute("courseID").toString()+'_'+task.getId();//新id
        if(!oldID.equals(id)){//如果改变了作业id，作业-学生表也需改变
            TSMapper.changeTaskID(id,oldID);
        }
        task.setId(id);
        if(!file.isEmpty()){//如果有文件更新文件
            String fileName = task.getId()+"_"+file.getOriginalFilename();//getOriginalFilename()此方法是获取原始文件名称
            file.transferTo(new File("D:/cosmos/tete/task/"+fileName));// MAC目录/Users/cosmos/Desktop/ Win10目录D:/cosmos/tete/
//            String url = "D:/cosmos/tete/task/"+fileName;
            task.setFileName(fileName);//将文件名加入数据库
            TSMapper.updateTaskF(task,oldID);
        }else {//如果没有文件不更新文件名
            TSMapper.updateTask(task,oldID);
        }
    }
    //批改作业
    @Override
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
    public void setInformation(Staff staff) {
        staff.setId(session.getAttribute("id").toString());//防止乱改
        TSMapper.TeaUpdateStaff(staff);
    }
    //获取教师今日课程表

    /**
     * schedule字段设计
     * 固定数组第一位(schedule[0])为课程周数范围，数组后面为每节课具体时间，以;号分开每节具体课程
     * 规定:
     *      起始周数(1-25),结束周数(1-25);单双周(0全1单2双),星期几(0-7),课程节数(必须连续),说明(实验/理论课,授课地点)
     * 例子:
     *      1,16;1,0,1-2,理论课A教502
     *      1-16周 ; 单周 星期日 第1,2节课 理论课 A教502
     */
    @Override
    public List<Map<String,String>> getTodayTeaSchoolTimetable() {
        List<Course> courses = TSMapper.queryTeaCourse(session.getAttribute("id").toString());
        Calendar calendar = Calendar.getInstance();
        if(!courses.isEmpty()){//判断老师是否有课
            List<Map<String,String>> list = new ArrayList<>();
            for(Course course:courses){//循环每种课
                if(course.getSchedule()!=null && !course.getSchedule().equals("")){//schedule字段表不为空
                    String[] a= course.getSchedule().split(";");
                    String[] range = a[0].split(",");
                    int startWeek = Integer.parseInt(range[0]);
                    int endWeek = Integer.parseInt(range[1]);
                    if(DateUtils.currentWeeks.intValue()>=startWeek &&
                            DateUtils.currentWeeks.intValue()<=endWeek){//判断当前周是否在课程周内
                        for (int i = 1; i < a.length; i++) {//循环此课的具体每节课
                            String[] Class= a[i].split(",");
                            int cal = calendar.get(Calendar.DAY_OF_WEEK)-1;
                            if ((Class[0].equals("0") ||
                                    Class[0].equals(DateUtils.oddEvenWeek)) &&
                                    Class[1].equals(Integer.toString(cal))) {//判断单双周,判断星期
                                Map<String,String> map = new HashMap<>();
                                map.put("name",course.getName());
                                map.put("lesson",Class[2]);
                                map.put("message",Class[3]);
                                list.add(map);
                            }
                        }
                    }
                }
            }
//            System.out.println(list);
//            System.out.print("今天星期");
//            System.out.println(calendar.get(Calendar.DAY_OF_WEEK)-1);
//            System.out.println("当前周数"+DateUtils.currentWeeks);
//            System.out.println("现在是"+DateUtils.oddEvenWeek+"周");
            return list;
        }
        return null;
    }
}
