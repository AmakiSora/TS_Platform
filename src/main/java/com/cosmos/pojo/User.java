package com.cosmos.pojo;

import lombok.Data;

/**
 * 用户表
 */
@Data
public class User {

    //用户id
//    private Integer id;

    //用户名
    private String username;

    //用户密码
    private String password;

    //用户权限
    private String role;

    //用户头像
    private byte[] avatar;

}
