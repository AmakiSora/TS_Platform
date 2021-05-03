package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.*;
import com.cosmos.service.MainService;
import com.cosmos.utils.DateUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;//加密
    @Value("${fileURI}")
    private String fileURI;
    //获取头像
    @Override
    public byte[] getAvatar(String id) {
        User user = userMapper.getAvatar(id);
        if(user!=null){
            byte[] b = user.getAvatar();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return b;
        }else {//默认头像
            User u = userMapper.getAvatar("Avatar");
            byte[] b = u.getAvatar();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return b;
        }
    }
    //上传头像
    @Override
    public String setAvatar(MultipartFile file,String id) throws IOException {
        if(file.isEmpty()){
            return null;
        }
        String type = file.getContentType();//获取文件后缀名
        if("image/jpeg".equals(type)||"image/png".equals(type)){//判断文件是不是图片
            byte[] bytes=file.getBytes();//将图片转换成二进制流
            User user = new User();
            id = session.getAttribute("id").toString();//防止篡改
            user.setUsername(id);
            user.setAvatar(bytes);
            userMapper.setAvatar(user);
            if(session.getAttribute("role")=="staff"){
                return "redirect:/staff/settings.html";
            }else if(session.getAttribute("role")=="student"){
                return "redirect:/student/settings.html";
            }else {
                return "redirect:/admin/settings.html";
            }
        }else {
            return null;
        }
    }
    //改密码
    @Override
    public int changePassword(Map<String, String> Password) {
//        Password.get("oldPassword").equals(userMapper.queryPasswordByName(session.getAttribute("id").toString()))
        if(passwordEncoder.matches(Password.get("oldPassword"),userMapper.queryPasswordByName(session.getAttribute("id").toString()))){
            if(Password.get("newPassword").equals(Password.get("confirmPassword"))){
                userMapper.changePassword(session.getAttribute("id").toString(),passwordEncoder.encode(Password.get("newPassword")));
                return 1;//改密成功
            }else {
                return 2;//新密码不一致
            }
        }else {
            return 3;//旧密码不正确
        }
    }
    //下载文件
    @Override
    public void download(String fileName, String url, HttpSession session, HttpServletResponse response) throws IOException {
//        String path = session.getServletContext().getRealPath("/task");//找到xx目录的实际路径
        String realPath = fileURI+url+"/"+fileName;
        response.setHeader("content-disposition","attachment;filename="+fileName);//设置响应头 告知浏览器要保存内容 filename=浏览器显示的下载文件名
        FileInputStream in= new FileInputStream(realPath);
        OutputStream out = response.getOutputStream();
        IOUtils.copy(in,out);
        out.close();//关闭流
        in.close();
//        IOUtils.closeQuietly(out);
//        IOUtils.closeQuietly(in);
//        System.gc();//调用jvm进行垃圾回收
    }
    //发表评论
    @Override
    public String discuss(String position, String text, HttpServletRequest request) {
        TSMapper.discuss(session.getAttribute("id").toString(),
                session.getAttribute("name").toString(),
                new Date(),text,position);
        if(position.equals(session.getAttribute("courseID").toString())){//返回课程评论区
            return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
        }
        return "redirect:"+request.getHeader("Referer");
    }
    //回复评论
    @Override
    public String reply(String position, String text, String replier, String replierID, HttpServletRequest request) {
        TSMapper.reply(session.getAttribute("id").toString(),
                session.getAttribute("name").toString(),
                new Date(),text,position,replier,replierID);
        TSMapper.upRepliesNum(Integer.valueOf(position));//评论数+1
        if(position.equals(session.getAttribute("courseID").toString())){//返回课程评论区
            return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
        }
        return "redirect:"+request.getHeader("Referer");
    }
    //删除评论
    @Override
    public String deleteComment(int NO, HttpServletRequest request) {
        TSMapper.deleteComment(NO);
        return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
    }
    //删除回复
    @Override
    public String deleteReply(int NO, int N, HttpServletRequest request) {
        TSMapper.deleteComment(NO);
        TSMapper.downRepliesNum(N);
        return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
    }
    //讨论内容
    @Override
    public void commentList(String position, Model model) {
        List<Comment> commentList = TSMapper.queryCommentList(position);
        for (Comment list:commentList){
            if(!list.getRepliesNum().equals(0)){//如果回复人数不为零
                String a = list.getNO().toString();
                model.addAttribute(a,TSMapper.queryCommentList(a));
            }
        }
        model.addAttribute("comment",commentList);
    }
    //作业列表
    @Override
    public List<Task> queryTaskList(String courseID) {
        List<Task> task = TSMapper.queryTaskList(courseID);
        Date now = new Date();
        for(Task list:task){
            Date i = list.getIssuedDate();
            Date j = list.getDeadline();
            if(now.after(i)&&now.before(j)){//进行中
                list.setState("1");
            }else if(now.after(j)){//已截止
                list.setState("2");
            }else if(now.before(i)){//未开始
                list.setState("3");
            }else{
                list.setState("4");
            }
        }
        return task;
    }
    //获取今日课程表
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
    public List<Map<String,String>> getTodayCoursesSchedule() {
        String role = session.getAttribute("role").toString();
        if(role.equals("staff")){
            List<Course> courses = TSMapper.queryTeaCourse(session.getAttribute("id").toString());
            return handleSchedule(courses,role);
        }else if(role.equals("student")){
            List<Course> courses = TSMapper.queryStuCourse(session.getAttribute("id").toString());
            return handleSchedule(courses,role);
        }else {
            return null;
        }
    }
    //处理今日课程表
    private List<Map<String, String>> handleSchedule(List<Course> courses,String role) {
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
                                map.put("link","/"+role+"/courses/"+course.getId());
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
            if(list.isEmpty()){
                return null;
            }else {
                return list;
            }

        }
        return null;
    }
    //首页校园新闻
    @Override
    public List<CampusNews> getCampusNewsList() {
        return TSMapper.getCampusNewsList();
    }
    //更多新闻
    @Override
    public List<CampusNews> getMoreCampusNewsList() {
        // TODO: 2021/4/24 新闻更多页分页 现在只是获得所有
        return TSMapper.getAllCampusNewsList();
    }
    //校园新闻详情
    @Override
    public CampusNews campusNewsDetails(Integer id) {
        TSMapper.upCampusNewsViews(id);
        return TSMapper.getcampusNewsDetails(id);
    }
}
