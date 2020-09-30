package com.geovis.core.shiro;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.geovis.core.constant.Constant;

/**
 * CrosFilter : 跨域资源共享过滤器, 该过滤器设置response header, 解决跨域ajax请求报错
 */
//@Component
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse)res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));// 允许所有域进行访问,可以指定多个Access-Control-Allow-Origin:http://localhost:8080/
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");// 允许的方法
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,Pragma, Token,Cache-Control, Authorization, username");
        response.setHeader("Access-Control-Expose-Headers", "content-type" + ", " + Constant.EXPOSE_HEADER);//如果不设置，js获取不到头信息
        response.setHeader("Access-Control-Allow-Credentials","true");
        //response.addHeader("XDomainRequestAllowed","1");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
