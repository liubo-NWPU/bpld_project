package com.gis.manager;
import com.gis.manager.utils.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @作者:liuyan
 * @时间:创建于2019/12/25 16:07
 * @描述:SpringBoot启动
 */
@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(nameGenerator = UniqueNameGenerator.class)
@EnableJms
public class Application  extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
}
