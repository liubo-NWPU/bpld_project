/**
 * @description:
 * @author: liuyan
 * @create: 2020-01-07 18:25
 **/

package com.gis.manager.annotation.mutijms;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MultiJmsListenerAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    //用以缓存方法中没有MultiJmsListener注解的
    private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(64));

    //JMS 注册机
    private JmsListenerEndpointRegistrar jmsListenerEndpointRegistrar = new JmsListenerEndpointRegistrar();

    private BeanFactory beanFactory;

    @Value("${threadnum}")
    private int threadNum;

    @Autowired
    public void setEndpointRegistry(JmsListenerEndpointRegistry endpointRegistry) {
        jmsListenerEndpointRegistrar.setEndpointRegistry(endpointRegistry);
    }

    @Autowired
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        jmsListenerEndpointRegistrar.setBeanFactory(beanFactory);
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //特殊bean过滤
        if((bean instanceof AopInfrastructureBean)
                || (bean instanceof JmsListenerContainerFactory)
                || (bean instanceof JmsListenerEndpointRegistry)) {
            return bean;
        }

        //获取bean对象的最终类型
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
        if (this.nonAnnotatedClasses.contains(targetClass)) {
            return bean;
        }

        //选出该bean中包含MultiJmsListener注解的方法
        Map<Method, MultiJmsListener> annotatedMethods = MethodIntrospector.selectMethods(targetClass, new MethodIntrospector.MetadataLookup<MultiJmsListener> () {
            @Override
            public MultiJmsListener inspect(Method method) {
                return AnnotationUtils.getAnnotation(method, MultiJmsListener.class);
            }
        });

        // 若当前类中未找到含该注解的方法，直接return bean
        if (annotatedMethods.isEmpty()) {
            this.nonAnnotatedClasses.add(targetClass);
            return bean;
        }

        //对每个方法做处理，添加监听
        annotatedMethods.forEach((method, multiJmsListener) -> processMultiListener(multiJmsListener, method, targetClass));

        //返回bean对象
        return bean;
    }

    /*
     * 提交监听器到进程
     * @param multiJmsListener 注解
     * @param method 方法
     * @param targetClass bean对应的class类
     * @param <T>
     */
    private <T> void processMultiListener(MultiJmsListener multiJmsListener, Method method, Class<T> targetClass) {
        //线程数
//        int threadNum = multiJmsListener.num();

        if (threadNum <= 0) {
            threadNum= 1;
        }

        //for循环
        for (int i = 1; i <= threadNum; i ++) {
            //多少个线程数，开启多少个监听
            ActiveMQMessageListener activeMQMessageListener = new ActiveMQMessageListener<>(targetClass, method, i, multiJmsListener.destination());
            //将监听器注册到JMSEndpointRegistrar
            register(multiJmsListener, activeMQMessageListener);
        }
    }

    /*
     * 向JMSEndpointRegistrar注册Endpoint
     * @param activeMQMessageListener 消息监听器
     */
    private void register(MultiJmsListener multiJmsListener, ActiveMQMessageListener activeMQMessageListener) {
        SimpleJmsListenerEndpoint simpleJmsListenerEndpoint = new SimpleJmsListenerEndpoint();
        //endpointId = 类名+方法名+id
        simpleJmsListenerEndpoint.setId(buildEndpointId(activeMQMessageListener));
        simpleJmsListenerEndpoint.setDestination(activeMQMessageListener.getDest());
        simpleJmsListenerEndpoint.setMessageListener(activeMQMessageListener);
        jmsListenerEndpointRegistrar.setBeanFactory(this.beanFactory);

        //不同的模式，引用的containerFactory不同
        String containFactory = multiJmsListener.containerFactory();
        if (StringUtils.isEmpty(containFactory)) {
            //默认使用Queue
            containFactory = ActiveMQConsumerConstant.CONTAINER_FACTORY_QUEUE;
        }
        JmsListenerContainerFactory<?> factory = (JmsListenerContainerFactory)this.beanFactory.getBean(containFactory, JmsListenerContainerFactory.class);
        jmsListenerEndpointRegistrar.setContainerFactoryBeanName(ActiveMQConsumerConstant.CONTAINER_FACTORY_QUEUE);
        jmsListenerEndpointRegistrar.registerEndpoint(simpleJmsListenerEndpoint, factory);

    }

    /*
     * 生成端点Endpoint的ID
     * @param activeMQMessageListener
     * @return
     */
    private String buildEndpointId(ActiveMQMessageListener activeMQMessageListener) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(activeMQMessageListener.getExecuteClass().getName())
                .append(".")
                .append(activeMQMessageListener.getExecuteMethod().getName())
                .append(activeMQMessageListener.getId());
        return stringBuilder.toString();
    }

    /**
     * 在类初始化后，执行后续操作
     */
    @Override
    public void afterSingletonsInstantiated() {
        //执行registrar的后置操作
        jmsListenerEndpointRegistrar.afterPropertiesSet();
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}


