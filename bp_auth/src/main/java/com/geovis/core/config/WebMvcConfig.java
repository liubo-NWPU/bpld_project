package com.geovis.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.geovis.core.interceptor.LogInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
    
}
