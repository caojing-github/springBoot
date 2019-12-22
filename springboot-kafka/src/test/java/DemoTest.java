import com.caojing.KafkaController;
import com.caojing.KafkaSendResultHandler;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootTest(classes = {KafkaController.class})
@RunWith(SpringRunner.class)
public class DemoTest {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * @Resource先按名称查找而@Autowired先按类型查找，详细差别见： https://blog.csdn.net/balsamspear/article/details/87936936
     */
    @Resource
    private KafkaTemplate defaultKafkaTemplate;

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private KafkaSendResultHandler producerListener;

    @Autowired
    private ReplyingKafkaTemplate replyingKafkaTemplate;

    /**
     * 创建主题，Topic的新增删除方法都是异步执行的，为了避免在创建过程中程序关闭导致创建失败，所以在代码最后加了一秒的休眠
     */
    @Test
    public void testCreateTopic() throws InterruptedException {
        NewTopic topic = new NewTopic("topic.quick.initial2", 1, (short) 1);
        adminClient.createTopics(Arrays.asList(topic));
        Thread.sleep(1000);
    }

    /**
     * 查询Topic信息
     */
    @Test
    public void testSelectTopicInfo() throws Exception {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList("topic.quick.initial"));
        result.all().get().forEach((k, v) -> System.out.println("k: " + k + " ,v: " + v.toString() + "\n"));
    }

    /**
     * 发送消息
     */
    @Test
    public void testDemo() throws InterruptedException {
        kafkaTemplate.send("topic.quick.demo", "this is my first demo");
        //休眠5秒，为了使监听器有足够的时间监听到topic的数据
        Thread.sleep(5000);
    }

    /**
     * 用设置过主题的KafkaTemplate发送
     * https://www.jianshu.com/p/9bf9809b7491
     */
    @Test
    public void testDefaultKafkaTemplate() {
        defaultKafkaTemplate.sendDefault("I`m send msg to default topic");
    }

    /**
     * 发送带有时间戳的消息、使用ProducerRecord发送消息、使用Message发送消息
     */
    @Test
    public void testTemplateSend() {
        // 发送带有时间戳的消息
        kafkaTemplate.send("topic.quick.demo", 0, System.currentTimeMillis(), "0", "send message with timestamp");

        // 使用ProducerRecord发送消息
        ProducerRecord record = new ProducerRecord("topic.quick.demo", "use ProducerRecord to send message");
        kafkaTemplate.send(record);

        // 使用Message发送消息
        Map map = new HashMap();
        map.put(KafkaHeaders.TOPIC, "topic.quick.demo");
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, "0");
        GenericMessage message = new GenericMessage("use Message to send message", new MessageHeaders(map));
        kafkaTemplate.send(message);
    }

    /**
     * 消息结果回调
     */
    @Test
    public void testProducerListen() throws InterruptedException {
        kafkaTemplate.setProducerListener(producerListener);
        kafkaTemplate.send("topic.quick.demo", "test producer listen");
        Thread.sleep(1000);
    }

    /**
     * 同步发送消息 https://www.jianshu.com/p/9bf9809b7491
     */
    @Test
    public void testSyncSend() throws ExecutionException, InterruptedException {
        kafkaTemplate.send("topic.quick.demo", "test sync send message").get();
    }

    /**
     * 使用事务（配置Kafka事务管理器并使用@Transactional注解）
     * https://www.jianshu.com/p/59891ede5f90
     */
    @Test
    @Transactional
    public void testTransactionalAnnotation() throws InterruptedException {
        kafkaTemplate.send("topic.quick.tran", "test transactional annotation");
        throw new RuntimeException("fail");
    }

    /**
     * 使用事务（使用KafkaTemplate的executeInTransaction方法）
     */
    @Test
    public void testExecuteInTransaction() throws InterruptedException {
        kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback() {
            @Override
            public Object doInOperations(KafkaOperations kafkaOperations) {
                kafkaOperations.send("topic.quick.tran", "test executeInTransaction");
                throw new RuntimeException("fail");
                //return true;
            }
        });
    }

    /**
     * 测试消费者容器
     */
    @Test
    public void test() throws InterruptedException {
        kafkaTemplate.send("topic.quick.bean", "send msg to beanListener");
        Thread.sleep(1000);
    }

    /**
     * 批量消费
     */
    @Test
    public void testBatch() throws InterruptedException {
        for (int i = 0; i < 12; i++) {
//            kafkaTemplate.send("topic.quick.batch", i + "");
            kafkaTemplate.send("topic.quick.batch.partition", i + "");
        }
    }

    /**
     * 注解方式获取消息头及消息体
     */
    @Test
    public void testAnno() throws InterruptedException {
        Map map = new HashMap<>();
        map.put(KafkaHeaders.TOPIC, "topic.quick.anno");
        map.put(KafkaHeaders.MESSAGE_KEY, 0 + "");
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.TIMESTAMP, System.currentTimeMillis());

        kafkaTemplate.send(new GenericMessage<>("test anno listener", map));
    }

    /**
     * 使用Ack机制确认消费
     */
    @Test
    public void testAck() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            kafkaTemplate.send("topic.quick.ack", i + "");
        }
    }

    /**
     * 消息转发
     */
    @Test
    public void testForward() {
        kafkaTemplate.send("topic.quick.target", "test @SendTo");
    }

    /**
     * 由于ReplyingKafkaTemplate也是通过监听容器实现的，所以响应时间可能会较慢，要注意选择合适的场景使用
     * <p>
     * 1、向"topic.quick.request"主题发送消息"this is a message"
     * 2、"topic.quick.request"主题收到消息处理后给主题"topic.quick.reply"发送消息"topic.quick.reply  reply : this is a message"
     */
    @Test
    public void testReplyingKafkaTemplate() throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>("topic.quick.request", "this is a message");
        // 转发到主题"topic.quick.reply"
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "topic.quick.reply".getBytes()));

        // 获取发送结果
        RequestReplyFuture<String, String, String> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
        SendResult<String, String> sendResult = replyFuture.getSendFuture().get();
        System.out.println("Sent ok: " + sendResult.getRecordMetadata());

        // 返回结果
        ConsumerRecord<String, String> consumerRecord = replyFuture.get();
        System.out.println("Return value: " + consumerRecord.value());
        Thread.sleep(20000);
    }

    /**
     * 消息过滤器
     */
    @Test
    public void testFilter() throws InterruptedException {
        kafkaTemplate.send("topic.quick.filter", System.currentTimeMillis() + "");
    }

    /**
     * 异常处理器
     */
    @Test
    public void testErrorHandler() {
        kafkaTemplate.send("topic.quick.error", "test error handle");
    }
}