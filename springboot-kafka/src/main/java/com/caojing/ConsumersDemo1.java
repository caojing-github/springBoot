package com.caojing;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 消费者 https://www.jianshu.com/p/a64defb44a23
 *
 * @author CaoJing
 * @date 2019/12/20 23:16
 */
@Slf4j
@Component
public class ConsumersDemo1 {

    @KafkaListener(topics = "topic.quick.initial")
    public void consumer1(ConsumerRecord<?, ?> record) {
        log.info("consumer1 receive" + record.toString());
    }

    @KafkaListener(topics = "test")
    public void consumer2(String msgData) {
        log.info("consumer2 receive" + msgData);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 上面groupId默认为空串,开发中要么以上面写法或者下面写法保持统一风格
    ///////////////////////////////////////////////////////////////////////////

    @KafkaListener(groupId = "group1", topics = "topic.quick.initial")
    public void consumer3(ConsumerRecord<?, ?> record) {
        log.info("consumer3 receive" + record.toString());
    }

    @KafkaListener(groupId = "group1", topics = "test")
    public void consumer4(String msgData) {
        log.info("consumer4 receive" + msgData);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 上面与下面写法等价
    ///////////////////////////////////////////////////////////////////////////

//    @KafkaListener(id = "group1", topics = "topic.quick.initial")
//    public void consumer5(ConsumerRecord<?, ?> record) {
//        log.info("consumer5 receive" + record.toString());
//    }
//
//    @KafkaListener(id = "group1", topics = "test")
//    public void consumer6(String msgData) {
//        log.info("consumer6 receive" + msgData);
//    }

    /**
     * 注解方式获取消息头及消息体
     */
    @KafkaListener(id = "anno", topics = "topic.quick.anno")
    public void annoListener(@Payload String data,
                             @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) Integer key,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                             @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts) {

        log.info("topic.quick.anno receive : \n" +
            "data : " + data + "\n" +
            "key : " + key + "\n" +
            "partitionId : " + partition + "\n" +
            "topic : " + topic + "\n" +
            "timestamp : " + ts + "\n"
        );

    }
}
