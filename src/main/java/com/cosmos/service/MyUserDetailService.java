package com.cosmos.service;

import com.cosmos.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;//数据库
    @Autowired
    private PasswordEncoder passwordEncoder;//加密

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userMapper.findByUserName(username);//从数据库取出用户名
//        if(!user.equals(username)){
//            throw new UsernameNotFoundException("错");
//        }
//        String password = passwordEncoder.encode();
//        return new User(user,password,AuthorityUtils
//                .commaSeparatedStringToAuthorityList("lv1,lv2"));
////
        if(!"111".equals(username)){
            throw new UsernameNotFoundException("错");
        }
        String password = passwordEncoder.encode("123");//密码验证
        return new User("111",password,AuthorityUtils
                .commaSeparatedStringToAuthorityList("lv1,lv2"));//权限
        }
    }


