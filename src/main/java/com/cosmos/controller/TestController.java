package com.cosmos.controller;

import org.springframework.stereotype.Controller;

@Controller
public class TestController {
//    @NewsAOP
//    @GetMapping("/t/{i}")
//    @ResponseBody
//    public void add1(@PathVariable("i")String i,String a){
//        System.out.println(i+"???");
//        System.out.println("add1");
//    }
//    @NewsAOP
//    @GetMapping("/s/{i}")
//    @ResponseBody
//    public void add2(@PathVariable("i")String i){
//        System.out.println("@@@@@");
//    }
//    @NewsAOP
//    @GetMapping("/g")
//    @ResponseBody
//    public void add3(){
//
//    }













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
