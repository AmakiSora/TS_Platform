package com.cosmos.mapper;

import com.cosmos.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User selectByName(String username);//通过名字查询用户
    void addUser(User user);//增加帐号
    void deleteUser(String username);//删除
    void resetPasswordUser(String username,String Password);//重置密码
    String queryPasswordByName(String username);//查询密码
    void changePassword(String username,String newPassword);//更改密码
    void setAvatar(User user);//设置头像
    User getAvatar(String username);//获得头像
    String queryRoleByName(String username);//根据用户名判断角色

}
