package com.cosmos.config;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;
public class LoginSuccessHandle implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)throws IOException {
        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//获取用户名
        String id = userDetails.getUsername();
        session.setAttribute("id",id);
//        System.out.println(TsMapper.queryNameById("121101"));//查数据库会报错，应该是项目启动顺序问题，bean没有加载
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        //获取到登陆者的权限，然后做跳转
        if (roles.contains("admin")){
            session.setAttribute("role","admin");
            response.sendRedirect("admin/index.html");
        }else if (roles.contains("staff")){
            session.setAttribute("role","staff");
            response.sendRedirect("staff/index.html");
        }else if (roles.contains("student")){
            session.setAttribute("role","student");
            response.sendRedirect("student/index.html");
        }else {
            response.sendRedirect("/404");
        }

    }
}
