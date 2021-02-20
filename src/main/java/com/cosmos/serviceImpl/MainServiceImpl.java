package com.cosmos.serviceImpl;

import com.cosmos.mapper.TSMapper;
import com.cosmos.pojo.Comment;
import com.cosmos.pojo.Task;
import com.cosmos.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class MainServiceImpl implements MainService {
    @Autowired
    private HttpSession session;
    @Autowired
    private TSMapper TSMapper;
    //讨论内容
    @Override
    public void commentList(String position, Model model) {
        List<Comment> commentList = TSMapper.queryCommentList(position);
        for (Comment list:commentList){
            if(!list.getRepliesNum().equals(0)){//如果回复人数不为零
                String a = list.getNO().toString();
                model.addAttribute(a,TSMapper.queryCommentList(a));
            }
        }
        model.addAttribute("comment",commentList);
    }
    //作业列表
    @Override
    public List<Task> queryTaskList(String courseID) {
        List<Task> task = TSMapper.queryTaskList(courseID);
        Date now = new Date();
        for(Task list:task){
            Date i = list.getIssuedDate();
            Date j = list.getDeadline();
            if(now.after(i)&&now.before(j)){//进行中
                list.setState("1");
            }else if(now.after(j)){//已截止
                list.setState("2");
            }else if(now.before(i)){//未开始
                list.setState("3");
            }else{
                list.setState("4");
            }
        }
        return task;
    }
}
