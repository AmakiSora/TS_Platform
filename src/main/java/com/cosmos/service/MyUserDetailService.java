package com.cosmos.service;

import com.cosmos.mapper.AdministratorsMapper;
import com.cosmos.pojo.Administrators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private AdministratorsMapper administratorsMapper;//数据库
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Administrators administrator = administratorsMapper.queryAdministratorsByName(username);
//        if(administrator== null){//数据库没有用户名时
//            throw new UsernameNotFoundException("用户名或密码错误");
//        }
//        String myPassword = passwordEncoder.encode(administrator.getUserpassword());
//        return (UserDetails) administrator;
        if(!"111".equals(username)){
            throw new UsernameNotFoundException("错");
        }
        String password = passwordEncoder.encode("123");
        return new User("111",password,AuthorityUtils
                .commaSeparatedStringToAuthorityList("lv1,lv2"));//权限
        }
    }


