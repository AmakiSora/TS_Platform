package com.cosmos.service;

import java.util.Map;

public interface NoticeService {
    //消息通知
    Map notice(String id);
    //清空通知
    Integer noticeClear(String id);
    //消息重定向x
    String noticeReturn(String X,String id);
    //消息重定向xy
    String noticeReturn(String X,String Y,String id);


}
