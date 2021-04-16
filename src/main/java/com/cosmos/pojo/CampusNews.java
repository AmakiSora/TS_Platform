package com.cosmos.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class CampusNews {//校园新闻
    private Integer id;//新闻id 数据库递增
    private String title;//标题
    private String content;//内容
    private Date datetime;//发布时间
    private String author;//作者
    private Integer views;//浏览量
}
