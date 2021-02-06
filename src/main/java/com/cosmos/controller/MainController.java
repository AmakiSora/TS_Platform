package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.Map;

@Controller
public class MainController {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    //分割线------------------------------------------------------------------------------------
    @RequestMapping("/Avatar/{id}")//显示用户头像
    public ResponseEntity<byte[]> myAvatar (@PathVariable("id")String id){
        User user=  userMapper.getAvatar(id);
        if(user.getAvatar()!=null){
            byte[] b = user.getAvatar();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<byte[]>(b,headers, HttpStatus.OK);
        }else {
            return null;
        }

    }

    @PostMapping("/setAvatar")//上传头像
    public String setAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        if(file.isEmpty()){
            return null;
        }
        String type = file.getContentType();//获取文件后缀名
        if("image/jpeg".equals(type)||"image/png".equals(type)){//判断文件是不是图片
            byte[] bytes=file.getBytes();//将图片转换成二进制流
            User user = new User();
            user.setUsername(session.getAttribute("id").toString());
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
    @PostMapping("/changePassword")
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
        IOUtils.closeQuietly(out);//关闭流
        IOUtils.closeQuietly(in);
        System.gc();//关闭流有bug，文件大于600kb时会一直占用，目前原因不明，只能调用jvm进行垃圾回收
    }
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
    @PostMapping("/reply/{position}")//回复评论
    public String reply(@PathVariable("position")String position,String text,String replier,HttpServletRequest request){
        TSMapper.reply(session.getAttribute("id").toString(),
                session.getAttribute("name").toString(),
                new Date(),text,position,replier);
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
}
