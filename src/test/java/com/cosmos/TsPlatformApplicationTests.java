package com.cosmos;

import com.cosmos.mapper.StaffMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class TsPlatformApplicationTests {
    @Autowired
    private StaffMapper staffMapper;
    @Test
    void contextLoads() throws SQLException {
        System.out.println(staffMapper.queryStaffById(1002));
    }

}
