package com.gis.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/3 15:32
 * @Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.gis.manager.controller"))//当前包路径
                .paths(PathSelectors.any())
                .build();

    }
    //构建api文档的详细信息函数
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("SHAPE要素点存储查询 API调用示例")//页面标题
                .contact(new Contact("liuyan","","liuyan@geovis.com.cn"))//创建人
                .version("1.0")//版本号
                .description("API调用示例")//描述
                .build();
    }
}
