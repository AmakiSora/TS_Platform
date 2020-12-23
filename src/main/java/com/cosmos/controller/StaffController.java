package com.cosmos.controller;

import com.cosmos.dao.StaffDao;
import com.cosmos.pojo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
public class StaffController {
    @Autowired
    StaffDao staffDao;

    //查询所有职工列表
    @RequestMapping("/staf")
    public String list(Model model){
        Collection<Staff> staff = staffDao.getAll();
        model.addAttribute("staf",staff);//放在请求域中
        return "staf/staff";//thymeleaf默认会拼串  classpath:/templates/xxx.html
    }
}
