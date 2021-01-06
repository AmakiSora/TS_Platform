package com.cosmos;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.fasterxml.jackson.databind.DatabindContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;

import java.sql.SQLException;

@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    //    SecurityContextImpl securityContext = (SecurityContextImpl) request
//            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
//    String username = UsernamePasswordAuthenticationToken
//    SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");

    @Test
    void contextLoads() throws SQLException {
//        System.out.println(TSMapper.queryStaffList());
//        System.out.println(TSMapper.queryStudentList());
        System.out.println(TSMapper.queryStudentList());
    }

}
