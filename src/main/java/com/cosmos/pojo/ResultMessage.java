package com.cosmos.pojo;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ResultMessage {
    private boolean isSystem;//判断是不是系统发的
    private Map<String,Object> key;
    private Date time;
    private Object message;
}
