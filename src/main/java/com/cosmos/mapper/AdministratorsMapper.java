package com.cosmos.mapper;

import com.cosmos.pojo.Administrators;
import com.cosmos.pojo.Staff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdministratorsMapper {
    List<Administrators> queryAdministratorsList();
    Administrators queryAdministratorsById(int userid);
    Administrators queryAdministratorsByName(String username);

//    int deleteByPrimaryKey(Integer userid);
//
//    int insert(Administrators record);
//
//    int insertSelective(Administrators record);
//
//    Administrators selectByPrimaryKey(Integer userid);
//
//    int updateByPrimaryKeySelective(Administrators record);
//
//    int updateByPrimaryKey(Administrators record);
}