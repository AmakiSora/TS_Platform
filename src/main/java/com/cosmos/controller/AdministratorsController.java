package com.cosmos.controller;

import com.cosmos.pojo.Administrators;
import com.cosmos.service.AdministratorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

//@Controller
//public class AdminController {
//    @RequestMapping("/{page}")
//    public  String showPage(@PathVariable String page){
//        return page;
//    }//没有拦截器情况下用
//}
@Controller
//@RequestMapping("/user")
public class AdministratorsController {
    @Autowired
    private AdministratorsService administratorsService;
    @PostMapping("/addUser")
    public String addUsers(Administrators administrators) {
        try{
        this.administratorsService.addUser(administrators);
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "redirect:/ok";
    }
}
