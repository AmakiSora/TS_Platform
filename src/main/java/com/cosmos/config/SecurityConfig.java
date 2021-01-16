package com.cosmos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
//                .successForwardUrl("/index")//登录后跳转路径
                .successHandler(new LoginSuccessHandle())
                ;
        http.authorizeRequests()
                .antMatchers("/login.html").permitAll()//不用登录的页面
                .antMatchers("/css/**", "/js/**", "/images/**", "/fonts/**","/static/favicon.ico").permitAll()//取消样式的拦截
                .antMatchers("/admin/**").hasAuthority("admin")//hasRole角色，不能以ROLE_开头,数据库要ROLE_开头
                .antMatchers("/student/**").hasAuthority("student")//hasAuthority权限
                .antMatchers("/staff/**").hasAuthority("staff")
                .anyRequest().authenticated()//拦截所有请求
                ;
//        http.rememberMe()//记住我功能，因为要存进数据库，暂时不用
//                .tokenValiditySeconds(100000)//自定义失效时间，默认两周
//                .rememberMeServices()//自定义功能实现逻辑
//                .userDetailsService(userService())//自定义登录逻辑
//                .tokenRepository(tokenRepository)//指定存储位置
//        ;
        http.sessionManagement()//会话
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)//会话配置，默认需要用时创建会话
                .invalidSessionUrl("/login.html")//会话超时后跳转
                .sessionFixation().migrateSession()//会话保护方式，默认每次登录账户都创建新会话，旧会话无效，将旧会话属性存进新会话中
                ;
        http.csrf().disable();//关闭csrf(跨站请求劫持)防护
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
