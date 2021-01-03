package com.cosmos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping("/admin/{admin}")
    public String admin(@PathVariable("admin") String admin){
        return "/admin/"+admin;
    }
}
