package com.cosmos;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Task;
import com.fasterxml.jackson.databind.DatabindContext;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    StringEncryptor encryptor;

    //    SecurityContextImpl securityContext = (SecurityContextImpl) request
//            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
//    String username = UsernamePasswordAuthenticationToken
//    SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");

    @Test
    void contextLoads() throws SQLException {

//        System.out.println(TSMapper.queryMyTaskFile("121102","A001_5"));
//        Map<String,Object> MyTask = TSMapper.queryMyTaskFile("121102","A001_4");
//        if(MyTask.isEmpty())
//        System.out.println(MyTask);
//        System.out.println(MyTask.get("submitDate"));
//        System.out.println(MyTask.get("submitDate").getClass());
//        System.out.println(MyTask.get("fileName"));
//        System.out.println(MyTask.get("score"));
//
//

//        System.out.println(MyTask.get());

//        String password = encryptor.encrypt("");//加密数据库密码
//        System.out.println(password);
        //Iin72SzbDDMw49y6EwyuPqiztQcbZJiGOjMp5pa4Ol7z3RMfhmmBU/vKXe/hbZx/1tZG1XnGGbxK9+UC9QZ+8SKZiZdxT5ev5v5Vt7Lv8nvtFIeRRNQa3rVDqTSrDY9rHH59oxkXIm503J2/M08rcw==
        //JUj0I76WRQIWrZa1bJj8Og0LAmlUi4zS
        //S9wJL+/9zyKw+wHtMcIBpXDS6cmcM8rVSTJU13NyfSA=

    }

}
