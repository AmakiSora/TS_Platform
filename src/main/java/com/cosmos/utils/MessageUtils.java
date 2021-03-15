package com.cosmos.utils;

import com.cosmos.pojo.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.Map;

public class MessageUtils {//在线聊天信息工具类
    public static String getMessage(boolean isSystem, Map<String,Object> key, Object message){
        try {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystem);
            resultMessage.setMessage(message);
            resultMessage.setTime(new Date());
            if(key != null){//如果有来源设置发送人
                resultMessage.setKey(key);
            }
            ObjectMapper mapper = new ObjectMapper();//转化成json
            return mapper.writeValueAsString(resultMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
