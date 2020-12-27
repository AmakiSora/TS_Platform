package com.cosmos.dao.impl;

import com.cosmos.dao.AdministratorsDao;
import com.cosmos.pojo.Administrators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AdministratorsDaoImpl extends AdministratorsDao {
    @Autowired
    private jdbcTemplate jdbcTemplate ;
    @Override
    public void insertUsers(Administrators administrators){
        String sql = "insert into administrators(userid,username) ";
    }
}
