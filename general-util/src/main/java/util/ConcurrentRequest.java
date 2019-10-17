package util;

import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 并发请求工具
 * <p>
 * https://mp.weixin.qq.com/s/61z5ZfcA-rc7cCm7v0YSDQ
 */
@Slf4j
@SuppressWarnings("all")
public class ConcurrentRequest {

    /**
     * 请求次数
     */
    private static final int n = 10;

    /**
     * 并发数
     */
    private static final int c = 10;

    /**
     * 发送http请求
     */
    @FunctionalInterface
    interface RequestFunction {
        void request();
    }

    /**
     * POST请求 路径有参数、有请求体
     */
    @Test
    public void test() throws Exception {
        start(() -> {
            // 发起请求
            try {
                demo1();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    /**
     * 添加客户跟进人
     */
    private void demo1() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("f09d1938f08711e98ab27cd30aeb1494");
        jsonArray.add("495543f5f00611e98ab27cd30aeb1494");

        String response = HttpUtils.httpPostJson("http://localhost:9186/pusher/api/v1/crm/saveCharge?id=ab7b6b69f0b211e98ab27cd30aeb1494", jsonArray.toJSONString());
        log.info(response);

//        log.info(EntityUtils.toString(httpResponse.getEntity()));
    }

    public static void start(RequestFunction function) throws Exception {

        Runnable taskTemp = new Runnable() {

            private volatile int iCounter;

            @Override
            public void run() {
                for (int i = 0; i < n; i++) {
                    try {
                        function.request();
                        iCounter++;
                        log.info(System.nanoTime() + " [" + Thread.currentThread().getName() + "] iCounter = " + iCounter);
                        Thread.sleep(100);
                    } catch (Exception e) {
                        log.error("请求异常", e);
                    }
                }
            }
        };

        ConcurrentRequest request = new ConcurrentRequest();
        request.startTaskAllInOnce(c, taskTemp);
    }

    public long startTaskAllInOnce(int threadNums, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(threadNums);
        for (int i = 0; i < threadNums; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        // 使线程在此等待，当开始门打开时，一起涌入门中
                        startGate.await();
                        try {
                            task.run();
                        } finally {
                            // 将结束门减1，减到0时，就可以开启结束门了
                            endGate.countDown();
                        }
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            };
            t.start();
        }
        long startTime = System.nanoTime();
        log.info(startTime + " [" + Thread.currentThread() + "] All thread is ready, concurrent going...");
        // 因开启门只需一个开关，所以立马就开启开始门
        startGate.countDown();
        // 等等结束门开启
        endGate.await();
        long endTime = System.nanoTime();
        log.info(endTime + " [" + Thread.currentThread() + "] All thread is completed.");
        return endTime - startTime;
    }
}