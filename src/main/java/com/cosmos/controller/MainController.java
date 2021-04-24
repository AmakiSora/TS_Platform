package com.cosmos.controller;

import com.cosmos.aspect.NoticeAOP;
import com.cosmos.service.MainService;
import com.cosmos.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {
    @Autowired
    private HttpSession session;
    @Autowired
    private MainService mainService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;
    //分割线------------------------------------------------------------------------------------
    @Cacheable(value = "Avatar",key = "'Avatar'+#id")//存进redis缓存减少请求次数
    @RequestMapping("/Avatar/{id}")//显示用户头像
    @ResponseBody
    public byte[] Avatar (@PathVariable("id")String id){
        return mainService.getAvatar(id);
    }
    @CacheEvict(value = "Avatar",key = "'Avatar'+#id")//清除缓存
    @PostMapping("/setAvatar/{id}")//上传头像
    public String setAvatar(@RequestParam("file") MultipartFile file,@PathVariable("id")String id) throws IOException {
        return mainService.setAvatar(file, id);
    }

    @ResponseBody
    @PostMapping("/changePassword")//改密码
    public int changePassword(@RequestBody Map<String,String> Password){
        return mainService.changePassword(Password);
    }

    // TODO: 2021/4/9 批量下载文件
    @RequestMapping("/download/{url}/{fileName}")//下载文件
    public void download(@PathVariable("fileName")String fileName,@PathVariable("url")String url, HttpSession session, HttpServletResponse response) throws IOException{
        mainService.download(fileName,url,session,response);
    }
    @NoticeAOP.comment
    @PostMapping("/discuss/{position}")//发表评论
    public String discuss(@PathVariable("position")String position,String text,HttpServletRequest request){
        return mainService.discuss(position,text,request);
    }
    @NoticeAOP.comment
    @PostMapping("/reply/{position}")//回复评论
    public String reply(@PathVariable("position")String position,String text,String replier,String replierID,HttpServletRequest request){
        return mainService.reply(position, text, replier, replierID, request);
    }
    @GetMapping("/deleteComment/{NO}")//删除评论
    public String deleteComment(@PathVariable("NO")int NO,HttpServletRequest request){
        return mainService.deleteComment(NO, request);
    }
    @GetMapping("/deleteReply/{NO}/{N}")//删除回复
    public String deleteReply(@PathVariable("NO")int NO,@PathVariable("N")int N,HttpServletRequest request){
        return mainService.deleteReply(NO, N, request);
    }
    @GetMapping("/notice")//消息通知
    @ResponseBody
    public Map notice(){
        return noticeService.notice(session.getAttribute("id").toString());
    }
    @GetMapping("/noticeClear")//清空通知
    @ResponseBody
    public Integer noticeClear(){
        return noticeService.noticeClear(session.getAttribute("id").toString());
    }
    @GetMapping("/notice/{X}")//消息重定向x
    public String noticeReturn(@PathVariable("X")String X){
        return noticeService.noticeReturn(X, session.getAttribute("id").toString());
    }
    @GetMapping("/notice/{X}/{Y}")//消息重定向xy
    public String noticeReturn(@PathVariable("X")String X,@PathVariable("Y")String Y){
        return noticeService.noticeReturn(X, Y,session.getAttribute("id").toString());
    }
    @GetMapping("/campusNewsMore")//更多新闻
    public String campusNewsMore(Model model){
        String role = session.getAttribute("role").toString();
        model.addAttribute("campusNewsList",mainService.getMoreCampusNewsList());
        if ("student".equals(role)){
            return "student/campusNews-more.html";
        }else if ("staff".equals(role)){
            return "staff/campusNews-more.html";
        }else {
            return "404";
        }
    }
    @GetMapping("/campusNewsDetails/{id}")//新闻详情
    public String campusNewsDetails(@PathVariable Integer id, Model model){
        String role = session.getAttribute("role").toString();
        model.addAttribute("campusNews",mainService.campusNewsDetails(id));
        if ("student".equals(role)){
            return "student/campusNews-details.html";
        }else if ("staff".equals(role)){
            return "staff/campusNews-details.html";
        }else {
            return "404";
        }
    }
}
