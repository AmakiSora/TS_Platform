package com.cosmos.mapper;

import com.cosmos.pojo.Staff;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StaffMapper {
    List<Staff> queryStaffList();
    Staff queryStaffById(int id);
}
