package com.cosmos.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class LoginSuccessHandle implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)throws IOException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        //获取到登陆者的权限，然后做跳转
        if (roles.contains("admin")){
            response.sendRedirect("/admin_Index");
            return;
        }else if (roles.contains("staff")){
            response.sendRedirect("/staff_Index");
            System.out.println();
            return;
        }else if (roles.contains("student")){
            response.sendRedirect("/student_Index");
            System.out.println();
            return;
        }else {
            response.sendRedirect("/403");
        }

    }
}
