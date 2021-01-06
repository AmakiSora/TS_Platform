package com.cosmos.controller;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class StaffController {
    @RequestMapping("/staff/{staff}")
    public String staff(@PathVariable("staff") String staff){
        return "/staff/"+staff;
    }
    @Autowired
    private TSMapper TSMapper;
    //查询所有职工列表
    @RequestMapping("/staff")
    public String queryStaffList(Model model){
        List<Staff> staffList = TSMapper.queryStaffList();
        model.addAttribute("staffList",staffList);
        return "staff/staff";
    }
//    @RequestMapping("/staffs")
//    public String staff(Model model){
//
//        //Collection<Staff> staffs = staffDao.getAll();
//        //model.addAttribute("staffs",staffs);//放在请求域中
//        return "staf/staff";//thymeleaf默认会拼串  classpath:/templates/xxx.html
//    }
}
