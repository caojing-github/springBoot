package com.caojing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ForwardListener {

    /**
     * @SendTo 利用ReplyTemplate转发下面👇方法返回值到对应的Topic中
     */
    @KafkaListener(groupId = "forward", topics = "topic.quick.target")
    @SendTo("topic.quick.real")
    public String forward(String data) {
        log.info("topic.quick.target  forward " + data + " to  topic.quick.real");
        return "topic.quick.target send msg : " + data;
    }

    @KafkaListener(groupId = "replyConsumer", topics = "topic.quick.request", containerFactory = "kafkaListenerContainerFactory")
    @SendTo
    public String replyListen(String msgData) {
        log.info("topic.quick.request receive : " + msgData);
        return "topic.quick.reply  reply : " + msgData;
    }

    /**
     * 监听topic.quick.reply
     */
    @Bean
    public KafkaMessageListenerContainer<String, String> replyContainer(@Autowired ConsumerFactory<String, String> consumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties("topic.quick.reply");
        return new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(@Autowired ProducerFactory<String, String> producerFactory, KafkaMessageListenerContainer<String, String> replyContainer) {
        ReplyingKafkaTemplate<String, String, String> template = new ReplyingKafkaTemplate<>(producerFactory, replyContainer);
        // 回复超时时间为10秒
        template.setReplyTimeout(10000);
        return template;
    }
}