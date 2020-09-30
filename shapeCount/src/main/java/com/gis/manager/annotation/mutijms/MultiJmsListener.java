/**
 * @description: 多线程监听消息队列自定义注解
 * @author: liuyan
 * @create: 2020-01-07 18:17
 **/

package com.gis.manager.annotation.mutijms;

import org.springframework.messaging.handler.annotation.MessageMapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MessageMapping
public @interface MultiJmsListener {
    //用以定义监听器beanName的前缀(保留字段)
    String prefixId() default "";

    String destination();

    //定义监听线程的个数
//    int num() default 1;

    //定义消息的ContainerFactory,实际值参考ActiveMQConstant
    String containerFactory() default "";
}


