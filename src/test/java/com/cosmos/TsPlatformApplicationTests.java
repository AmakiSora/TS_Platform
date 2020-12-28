package com.cosmos;

import com.cosmos.mapper.AdministratorsMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    AdministratorsMapper administratorsMapper;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(administratorsMapper.selectByPrimaryKey(1001));
    }

}
