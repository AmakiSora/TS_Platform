package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.pojo.User;
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

import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    }
}
