/**
 * @description:
 * @author: liuyan
 * @create: 2020-01-07 18:24
 **/
package com.gis.manager.annotation.mutijms;

public interface ActiveMQConsumerConstant {
    /** Queue模式下的JmsListenerContainerFactory的Bean名称 */
    String CONTAINER_FACTORY_QUEUE = "jmsListenerContainerQueue";

    /** Topic模式下的JmsListenerContainerFactory的Bean名称 */
    String CONTAINER_FACTORY_TOPIC = "jmsListenerContainerTopic";
}
