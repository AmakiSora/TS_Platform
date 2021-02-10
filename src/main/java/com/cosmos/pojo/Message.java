package com.cosmos.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class Message {
    private Map<String,Object> key;
    private String message;
}
