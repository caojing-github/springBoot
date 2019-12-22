package com.caojing;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaInitialConfiguration {

    public static final String brokerList = "localhost:9092";

    /**
     * 主题创建第1种方式
     * 创建TopicName为topic.quick.initial的Topic并设置分区数为8以及副本数为1
     * 该方法也可以修改主题分区，修改分区数并不会导致数据的丢失，但是分区数只能增大不能减小。
     */
    @Bean
    public NewTopic initialTopic() {
        return new NewTopic("topic.quick.initial", 8, (short) 1);
    }

    @Bean
    public NewTopic initialTopic2() {
        return new NewTopic("test", 8, (short) 1);
    }

    /**
     * 第2种创建主题方式，需要以下版本依赖
     */
//        <dependency>
//            <groupId>org.springframework.kafka</groupId>
//            <artifactId>spring-kafka</artifactId>
//            <version>2.3.1.RELEASE</version>
//        </dependency>
//    @Bean
//    public NewTopic topic2() {
//        return TopicBuilder.name("topic2").partitions(1).replicas(1).build();
//    }
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        // 配置Kafka实例的连接地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        KafkaAdmin admin = new KafkaAdmin(props);
        return admin;
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }
}