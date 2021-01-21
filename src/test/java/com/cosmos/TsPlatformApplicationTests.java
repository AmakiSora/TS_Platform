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
//        System.out.println(TSMapper.queryStaffList());
//        System.out.println(TSMapper.queryStudentList());
        List<Task> task = TSMapper.queryTaskList("A002");//作业
        System.out.println(task);
        for(Task list:task) {
            Date i = list.getIssuedDate();
            Date j = list.getDeadline();
            list.setState("1");
        }
        System.out.println(task);



//        String password = encryptor.encrypt("");//加密数据库密码
//        System.out.println(password);//
    }

}
