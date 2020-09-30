package com.gis.trans.config;

import com.google.common.base.Predicate;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ma.gh
 * @Description
 * @date 2019-02-18 10:12:31
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {

        Predicate<RequestHandler> predicate = new Predicate<RequestHandler>() {
            @Override public boolean apply(RequestHandler input) {
                if (input.isAnnotatedWith(ApiOperation.class)) {//只有添加了ApiOperation注解的method才在API中显示
                    return true;
                } else {
                    return false;
                }
            }
        };

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(predicate) //只有添加了ApiOperation注解的method才在API中显示
                //.apis(RequestHandlerSelectors.basePackage("com.geovis.plugins.plugin.controller")) //显示全部接口
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("专题图管理api文档")
                .description("专题图管理")
                .termsOfServiceUrl("http://localhost:8053/swagger-ui.html")
                .version("1.0")
                .build();
    }
}