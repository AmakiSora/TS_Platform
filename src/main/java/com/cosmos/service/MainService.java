package com.cosmos.service;

import com.cosmos.pojo.Task;
import org.springframework.ui.Model;

import java.util.List;

public interface MainService {
    //讨论内容
    void commentList(String position,Model model);
    //作业列表
    List<Task> queryTaskList(String courseID);

}