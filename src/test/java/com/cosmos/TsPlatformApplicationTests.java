package com.cosmos;

import com.cosmos.mapper.TSMapper;
import com.cosmos.mapper.UserMapper;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    private TSMapper TSMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    StringEncryptor encryptor;
    @Test
    void contextLoads(){
//        String password = encryptor.encrypt("");//加密数据库密码
//        System.out.println(password);
    }

}
