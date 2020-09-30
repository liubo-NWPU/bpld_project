package com.geovis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * 启动类
 *
 * @version v.0.1
 */
@SpringBootApplication
@EnableTransactionManagement
//@EnableAutoConfiguration
public class GeovisApplication extends SpringBootServletInitializer {

    //public static void main(String[] args) {
        //SpringApplication.run(GeovisApplication.class, args);
    //}

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){

        return application.sources(GeovisApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(GeovisApplication.class, args);
    }
}
