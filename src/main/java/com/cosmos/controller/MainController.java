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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        byte[] b = user.getAvatar();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(b,headers, HttpStatus.OK);
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
                return "/staff/settings.html";
            }else if(session.getAttribute("role")=="student"){
                return "/student/settings.html";
            }else {
                return "/admin/settings.html";
            }
        }else {
            return null;
        }

    }
    @RequestMapping("/download/{fileName}")//下载文件
    public void download(@PathVariable("fileName")String fileName, HttpSession session, HttpServletResponse response) throws IOException{
//        String path = session.getServletContext().getRealPath("/task");//找到xx目录的实际路径
        String realPath = "D:/cosmos/tete/"+fileName;
        response.setHeader("content-disposition","attachment;filename="+fileName);//设置响应头 告知浏览器要保存内容 filename=浏览器显示的下载文件名
        IOUtils.copy(new FileInputStream(realPath),response.getOutputStream());
    }
}
