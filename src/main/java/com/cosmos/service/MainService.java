package com.cosmos.service;

import com.cosmos.pojo.CampusNews;
import com.cosmos.pojo.Task;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface MainService {
    //获取头像
    byte[] getAvatar(String id);
    //上传头像
    String setAvatar(MultipartFile file,String id) throws IOException;
    //改密码
    int changePassword(Map<String,String> Password);
    //下载文件
    void download(String fileName, String url, HttpSession session, HttpServletResponse response) throws IOException;
    //发表评论
    String discuss(String position, String text, HttpServletRequest request);
    //回复评论
    String reply(String position,String text,String replier,String replierID,HttpServletRequest request);
    //删除评论
    String deleteComment(int NO,HttpServletRequest request);
    //删除回复
    String deleteReply(int NO,int N,HttpServletRequest request);
    //讨论内容
    void commentList(String position,Model model);
    //作业列表
    List<Task> queryTaskList(String courseID);
    //获取今日课程表
    List<Map<String,String>> getTodayCoursesSchedule();
    //首页校园新闻
    List<CampusNews> getCampusNewsList();
    //校园新闻详情
    String campusNewsDetails(Integer id, Model model);
}
