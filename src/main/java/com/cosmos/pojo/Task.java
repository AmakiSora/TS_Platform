package com.cosmos.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class Task {
    private String id;//作业id
    private String name;//作业名
    private String courseID;//课程id
    private Date issuedDate;//发布日期
    private Date deadline;//截止日期
    private String fileName;//作业文件名
    //以下在数据库中不存储
    private String state;//作业状态：已截止，进行中，未开始
}
