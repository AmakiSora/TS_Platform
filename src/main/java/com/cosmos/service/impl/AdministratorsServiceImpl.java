package com.cosmos.service.impl;

import com.cosmos.mapper.administratorsMapper;
import com.cosmos.pojo.Administrators;
import com.cosmos.service.AdministratorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorsServiceImpl implements AdministratorsService {
    @Autowired
    private com.cosmos.mapper.administratorsMapper administratorsMapper;

    @Override
    public void addUser(Administrators administrators){
        this.administratorsMapper.insert(administrators);
    }
}
