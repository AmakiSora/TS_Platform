package com.cosmos.controller;

import com.cosmos.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class TestController {
//    public static final int ERROR = 0;
//    public static final int SUCCESS = 1;
//    @RequestMapping("/{target}")
//    public String forward(@PathVariable("target") String target){
//        return "/"+target;
//    }

    /**
     * 测试ajax
     * @param user
     * @return
     * @Author zhong
     */
//    @RequestMapping("/demo/login")//ajax测试
//    @ResponseBody
//    public int login(@RequestBody User user){
//        System.out.println("请求登录的用户名为: "+user.getUsername());
//        System.out.println("请求登录的密码为："+user.getPassword());
//        if (user.getUsername().equals("zhong")&&user.getPassword().equals("123456")){
//            return SUCCESS;
//        } else {
//            return ERROR;
//        }
//    }

//    @RequestMapping("/admin_Index")
//    public String admin_Index(){
//        return "/admin/index.html";
//    }
//    @RequestMapping("/staff_Index")
//    public String staff_Index(){
//        return "/staff/index.html";
//    }
//    @RequestMapping("/student_Index")
//    public String student_Index(){
//        return "/student/index.html";
//    }
}
//import javax.servlet.http.HttpSession;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestParam;
//@Controller//旧的登录方式
//public class LoginController {
//    @RequestMapping("/user/login")
//    public String login(
//            @RequestParam("username")String username,
//            @RequestParam("password")String password,
//            Model model, HttpSession session){
//        System.out.printf("username=" + username);
//        System.out.printf("password=" + password);
//        if (!username.isEmpty() && "123".equals(password)) {
//            session.setAttribute("loginUser",username);
//            return "redirect:/index";//redirect:/index
//        } else {
//            model.addAttribute("msg", "用户名或密码错误!");
//            return "login";
//        }
//    }
//}
//
