package com.caojing;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * 异常处理器
 */
@Slf4j
@Component
public class ErrorListener {

    @KafkaListener(groupId = "err", topics = "topic.quick.error", errorHandler = "consumerAwareErrorHandler")
    public void errorListener(String data) {
        log.info("topic.quick.error  receive : " + data);
        throw new RuntimeException("fail");
    }

    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException e, Consumer<?, ?> consumer) {
                log.error("consumerAwareErrorHandler receive : " + message.getPayload().toString());
                return null;
            }
        };
    }

}
