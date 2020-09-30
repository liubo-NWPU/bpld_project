package com.gis.manager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * @Author:wangmeng
 * @Date:创建于2019/4/22 10:46
 * @Description:
 */
public class UniqueNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return definition.getBeanClassName();
    }
}
