package com.cosmos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean//密码加密
    public PasswordEncoder pw(){
        return new BCryptPasswordEncoder();
    }
    @Override//授权
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()//自定义登录页面
                .loginPage("/login.html")//自定义登录页面设置
                .loginProcessingUrl("/login")//登录访问路径
                .successForwardUrl("/index")//登录后跳转路径
                ;
        http.authorizeRequests()
                .antMatchers("/login.html").permitAll()//不用登录的页面
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**","/favicon.ico").permitAll()//取消样式的拦截
                .antMatchers("/admin").hasRole("admin")//hasRole角色，不能以ROLE_开头
                .antMatchers("/student").hasRole("student")//hasAuthority权限
                .antMatchers("/staff").hasRole("staff")
                .anyRequest().authenticated()//所有请求都被拦截
                ;
        http.csrf().disable();//关闭csrf防护
    }

//    @Autowired
//    AdministratorsMapper administratorsMapper;
//    @Override//认证
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()//数据库
////                .inMemoryAuthentication()//启用内存存储用户
//                .withUser("123").password(new BCryptPasswordEncoder().encode("123")).roles("admin").and()
//                .withUser("321").password(new BCryptPasswordEncoder().encode("321")).roles("staff").and()
//        ;
//    }
}
