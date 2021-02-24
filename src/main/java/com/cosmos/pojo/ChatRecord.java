package com.cosmos.pojo;

import lombok.Data;

import java.util.Date;


@Data
public class ChatRecord {//聊天记录
    private Integer NO;
    private String id;
    private String message;
    private Date time;
    private String chatID;
}
