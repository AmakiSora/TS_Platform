package com.cosmos;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;
import com.cosmos.pojo.Task;
import com.fasterxml.jackson.databind.DatabindContext;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate redisTemplate;

    //    SecurityContextImpl securityContext = (SecurityContextImpl) request
//            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
//    String username = UsernamePasswordAuthenticationToken
//    SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");

    @Test
    void contextLoads() throws SQLException {
//        redisTemplate.opsForValue().set("hello","yes");
//        System.out.println(redisTemplate.opsForHash().keys("news1101"));
//        System.out.println(redisTemplate.opsForHash().size("news1101"));
//        redisTemplate.opsForHash().put("news121102","2","<a href=\"/student/index.html\">555</a>");
//
//        System.out.println(redisTemplate.opsForHash().size("news1101"));
//
//        redisTemplate.delete("news121102");



//        String password = encryptor.encrypt("");//加密数据库密码
//        System.out.println(password);

    }

}
