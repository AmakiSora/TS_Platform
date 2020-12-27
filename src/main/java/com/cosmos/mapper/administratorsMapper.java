package com.cosmos.mapper;

import com.cosmos.pojo.Administrators;

public interface administratorsMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(Administrators record);

    int insertSelective(Administrators record);

    Administrators selectByPrimaryKey(Integer userid);

    int updateByPrimaryKeySelective(Administrators record);

    int updateByPrimaryKey(Administrators record);
}