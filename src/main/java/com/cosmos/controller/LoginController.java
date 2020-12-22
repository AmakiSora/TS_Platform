package com.cosmos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
@Controller
public class LoginController {
    @RequestMapping("/user/login")
    public String login(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            Model model, HttpSession session){
        System.out.printf("username=" + username);
        System.out.printf("password=" + password);
        if (!username.isEmpty() && "123".equals(password)) {
            session.setAttribute("loginUser",username);
            return "redirect:/index";//redirect:/index
        } else {
            model.addAttribute("msg", "用户名或密码错误!");
            return "login";
        }
    }
}
