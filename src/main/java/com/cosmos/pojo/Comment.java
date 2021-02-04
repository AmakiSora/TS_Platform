package com.cosmos.pojo;

import lombok.Data;

import java.util.Date;


@Data
public class Comment {
    private Integer NO;//唯一序号
    private String id;//学号或教工号
    private String name;//名字
    private Date date;//发言时间
    private String text;//内容
    private String position;//发言位置
    private String state;//状态：已删除，正常
    private Integer repliesNum;//回复人数
    private String replier;//被回复者
}
