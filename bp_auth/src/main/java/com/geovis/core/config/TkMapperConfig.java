	package com.geovis.core.config;

    import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

@Configuration
@AutoConfigureAfter(MybatisConfig.class)
public class TkMapperConfig {

    //mappers（dao)的类路径
    //private String mapperBasePackage ="com.geovis.web.domain.**";
    private String mapperBasePackage ="com.geovis.web.dao.**.mapper";
    //通用Mapper的接口类
    private String mappers = "com.geovis.web.base.mapper.BaseMapper";

    private String identity = "SELECT REPLACE(UUID_GENERATE_V4()||'''',''-'','''')";

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        //String configName = environment.resolvePlaceholders("${spring.cloud.bootstrap.name:bootstrap}");
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        mapperScannerConfigurer.setBasePackage(mapperBasePackage);

        Properties properties = new Properties();
        //通用mapper位置，不要和其他mapper、dao放在同一个目录
        properties.setProperty("mappers", mappers);
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY",identity);
        //properties.setProperty("IDENTITY","SELECT REPLACE(UUID(),''-'','''')");
        //properties.setProperty("IDENTITY", "'"+IdGenUtil.uuid()+"'");
        //主键UUID回写方法执行顺序,默认AFTER,可选值为(BEFORE|AFTER)
        properties.setProperty("ORDER","BEFORE");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

}