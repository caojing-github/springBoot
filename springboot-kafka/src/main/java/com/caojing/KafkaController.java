package com.caojing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名 xin.csqsx.restful
 * 类名 KafkaController
 * 类描述 springBoot整合kafka
 *
 * @author dell
 * @version 1.0
 * 创建日期 2017/12/15
 * 时间 11:55
 */
@Slf4j
@RestController
@SpringBootApplication
public class KafkaController {

    public static void main(String[] args) {
        SpringApplication.run(KafkaController.class, args);
    }

    /**
     * 注入kafkaTemplate
     */
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息
     * curl http://localhost:8080/produce
     */
    @RequestMapping("/produce")
    public String testKafka() {
        int iMax = 6;
        for (int i = 1; i < iMax; i++) {
            kafkaTemplate.send("test", "key" + i, "data" + i);
        }
        return "success";
    }
}
