/**
 * @description:
 * @author: liuyan
 * @create: 2020-01-07 18:34
 **/

package com.gis.manager.annotation.mutijms;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;



@Configuration
public class ActiveMQMultiJmsConfig {

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER,ActiveMQConnectionFactory.DEFAULT_PASSWORD,
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        return activeMQConnectionFactory;
    }
    /**
     * 配置topic模式下的JmsListenerContainerFactory
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean(name = ActiveMQConsumerConstant.CONTAINER_FACTORY_TOPIC)
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //支持Topic模式
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }
    /**
     * 配置queue模式下的JmsListenerContainerFactory
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean(name = ActiveMQConsumerConstant.CONTAINER_FACTORY_QUEUE)
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }

    /**
     * 用以支持MultiJmsListener注解的后置处理器
     * @return
     */
    @Bean
    public MultiJmsListenerAnnotationBeanPostProcessor multiJmsListenerAnnotationBeanPostProcessor() {
        MultiJmsListenerAnnotationBeanPostProcessor multiJmsBeanPostProcessor = new MultiJmsListenerAnnotationBeanPostProcessor();
        return multiJmsBeanPostProcessor;
    }
}


