package com.cosmos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Controller
public class MyMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login.html").setViewName("login");
//        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");//让其他链接能返回
//        registry.addViewController("/DIY.html").setViewName("DIY");//网页模版
    }


//    @Override//登录拦截器
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginHandlerInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login.html", "/", "/user/login", "/css/*", "/js/**", "/images/**", "/fonts/**");
//    }
}
