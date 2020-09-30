/**
 * @description:
 * @author: liuyan
 * @create: 2020-01-07 18:19
 **/

package com.gis.manager.annotation.mutijms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.lang.reflect.Method;

public class ActiveMQMessageListener<T> implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(ActiveMQMessageListener.class);

    //消息监听ID
    private int id;

    //执行方法
    private Method executeMethod;

    //执行类
    private Object executeObject;

    //监听目标
    private String dest;

    private Class<T> executeClass;

    /** 有参构造器 */
    public ActiveMQMessageListener(Class<T> executeClass, Method executeMethod, int id, String dest) {
        this.executeMethod = executeMethod;
        this.dest = dest;
        this.id = id;
        init(executeClass);
        this.executeClass = executeClass;
    }

    /**
     * 初始化操作
     * @param executeClass
     */
    private void init(Class<T> executeClass) {
        try {
            this.executeObject = executeClass.newInstance();
        } catch (Exception e) {
//            throw new JmsException("方法反射异常:", e);
        }
    }

    public ActiveMQMessageListener() {}

    @Override
    public void onMessage(Message message) {
        if (logger.isDebugEnabled()) {
            logger.debug("监听器[{}]监听到消息", id);
        }
        TextMessage textMessage = (TextMessage) message;
        try {
            //反射的方式调用方法，用以执行实际消费流程
            executeMethod.invoke(executeObject, textMessage.getText());
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("消息监听异常:", e);
            }
//            throw new JmsException("消息监听异常:", e);
        }
    }

    public Logger getLogger() {
        return logger;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Method getExecuteMethod() {
        return executeMethod;
    }

    public void setExecuteMethod(Method executeMethod) {
        this.executeMethod = executeMethod;
    }

    public Object getExecuteObject() {
        return executeObject;
    }

    public void setExecuteObject(Object executeObject) {
        this.executeObject = executeObject;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public Class<T> getExecuteClass() {
        return executeClass;
    }

    public void setExecuteClass(Class<T> executeClass) {
        this.executeClass = executeClass;
    }
}


