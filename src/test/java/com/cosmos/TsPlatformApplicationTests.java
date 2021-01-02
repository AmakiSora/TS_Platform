package com.cosmos;

import com.cosmos.mapper.StaffMapper;
import com.cosmos.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    private StaffMapper staffMapper;
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(userMapper.selectByName("admin"));
    }

}
