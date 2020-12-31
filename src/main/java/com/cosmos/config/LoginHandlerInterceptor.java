package com.cosmos.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//public class LoginHandlerInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
//        //登陆成功的sessoin
//        Object loginUser = request.getSession().getAttribute("loginUser");
//        if (loginUser == null){//没有登陆
//            request.setAttribute("msg","请先登录");
//            request.getRequestDispatcher("/").forward(request,response);
//            return false;
//        }else {
//            return true;
//        }
//    }
//}
