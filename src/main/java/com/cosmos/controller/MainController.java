package com.cosmos.controller;

import com.cosmos.aspect.NewsAOP;
import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    //分割线------------------------------------------------------------------------------------
    @Cacheable(value = "Avatar",key = "'Avatar'+#id")//存进redis缓存减少请求次数
    @RequestMapping("/Avatar/{id}")//显示用户头像
    @ResponseBody
    public byte[] Avatar (@PathVariable("id")String id){
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
    @CacheEvict(value = "Avatar",key = "'Avatar'+#id")//清除缓存
    @PostMapping("/setAvatar/{id}")//上传头像
    public String setAvatar(@RequestParam("file") MultipartFile file,@PathVariable("id")String id) throws IOException {
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

    @ResponseBody
    @PostMapping("/changePassword")//改密码
    public int changePassword(@RequestBody Map<String,String> Password){
        if(Password.get("oldPassword").equals(userMapper.queryPasswordByName(session.getAttribute("id").toString()))){
            if(Password.get("newPassword").equals(Password.get("confirmPassword"))){
                userMapper.changePassword(session.getAttribute("id").toString(),Password.get("newPassword"));
                return 1;//改密成功
            }else {
                return 2;//新密码不一致
            }
        }else {
            return 3;//旧密码不正确
        }
    }

    @RequestMapping("/download/{url}/{fileName}")//下载文件
    public void download(@PathVariable("fileName")String fileName,@PathVariable("url")String url, HttpSession session, HttpServletResponse response) throws IOException{
//        String path = session.getServletContext().getRealPath("/task");//找到xx目录的实际路径
        String realPath = "D:/cosmos/tete/"+url+"/"+fileName;
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
    @NewsAOP.comment
    @PostMapping("/discuss/{position}")//发表评论
    public String discuss(@PathVariable("position")String position,String text,HttpServletRequest request){
        TSMapper.discuss(session.getAttribute("id").toString(),
                session.getAttribute("name").toString(),
                new Date(),text,position);
        if(position.equals(session.getAttribute("courseID").toString())){//返回课程评论区
            return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
        }
        return "redirect:"+request.getHeader("Referer");
    }
    @NewsAOP.comment
    @PostMapping("/reply/{position}")//回复评论
    public String reply(@PathVariable("position")String position,String text,String replier,String replierID,HttpServletRequest request){
        TSMapper.reply(session.getAttribute("id").toString(),
                session.getAttribute("name").toString(),
                new Date(),text,position,replier,replierID);
        TSMapper.upRepliesNum(Integer.valueOf(position));//评论数+1
        if(position.equals(session.getAttribute("courseID").toString())){//返回课程评论区
            return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
        }
        return "redirect:"+request.getHeader("Referer");
    }
    @GetMapping("/deleteComment/{NO}")//删除评论
    public String deleteComment(@PathVariable("NO")int NO,HttpServletRequest request){
        TSMapper.deleteComment(NO);
            return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
    }
    @GetMapping("/deleteReply/{NO}/{N}")//删除回复
    public String deleteReply(@PathVariable("NO")int NO,@PathVariable("N")int N,HttpServletRequest request){
        TSMapper.deleteComment(NO);
        TSMapper.downRepliesNum(N);
        return "redirect:"+request.getHeader("Referer")+"#Courses-comment";
    }
    @GetMapping("/news")//消息通知
    @ResponseBody
    public Map news(){
        String key="news"+session.getAttribute("id").toString();
        long i = redisTemplate.opsForHash().size(key);
        if(i == 0){//没有通知
            return null;
        }else {//有通知
            Map map = redisTemplate.opsForHash().entries(key);
            return map;
        }
    }
    @GetMapping("/newsClear")//清空通知
    @ResponseBody
    public Integer newsClear(){
        String id = session.getAttribute("id").toString();
        redisTemplate.delete("news"+id);
        return 1;
    }
    @GetMapping("/news/{X}")//消息重定向
    public String newsReturn(@PathVariable("X")String X){
        String role = userMapper.queryRoleByName(session.getAttribute("id").toString());
        if (role.equals("student")){
            return "redirect:/student/"+X;
        }else if (role.equals("staff")){
            return "redirect:/staff/"+X;
        }else {
            return null;
        }
    }
    @GetMapping("/news/{X}/{Y}")//消息重定向
    public String newsReturn(@PathVariable("X")String X,@PathVariable("Y")String Y){
        String role = userMapper.queryRoleByName(session.getAttribute("id").toString());
        System.out.println(X+"/"+Y);
        if (role.equals("student")){
            return "redirect:/student/"+X+"/"+Y;
        }else if (role.equals("staff")){
            return "redirect:/staff/"+X+"/"+Y;
        }else {
            return null;
        }
    }

//    @RequestMapping("/send/news")//测试发消息
//    @ResponseBody
//    public String sendNews(){
//        Map map = new HashMap();
//        map.put("1","第一条通知");
//        map.put("2","第二条通知");
//        redisTemplate.opsForHash().put("news1101","1","第一条通知");
//        redisTemplate.opsForHash().put("news1101","2","第2条通知");
//        redisTemplate.opsForHash().put("news1102","1","第1条通知");
//
//        return "";
//    }
}
