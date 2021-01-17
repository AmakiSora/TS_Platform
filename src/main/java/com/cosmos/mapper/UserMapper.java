package com.cosmos.mapper;

import com.cosmos.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    /**
     * 通过用户名查找该账号
     * @param username
     * @return
     */
    User selectByName(String username);
    void addUser(User user);//增加帐号
    void deleteUser(String username);//删除
    void resetPasswordUser(String username);//更改信息
    void setAvatar(User user);//设置头像
    User getAvatar(String username);//获得头像
}
