package com.cosmos.ws;

import com.cosmos.pojo.ResultMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageUtils {//工具类
    public static String getMessage(boolean isSystem,String fromName,Object message){
        try {
            ResultMessage resultMessage = new ResultMessage();
            resultMessage.setSystem(isSystem);
            resultMessage.setMessage(message);
            if(fromName != null){//如果有来源设置发送人
                resultMessage.setName(fromName);
            }
            ObjectMapper mapper = new ObjectMapper();//转化成json
            return mapper.writeValueAsString(resultMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }
}
