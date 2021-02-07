package com.cosmos.pojo;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean isSystem;//判断是不是系统发的
    private String name;
    private Object message;
}
