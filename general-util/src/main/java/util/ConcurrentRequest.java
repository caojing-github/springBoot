package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static util.JdbcUtil.DataSource.PUSHER_DEV;

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
    private static final int n = 1;

    /**
     * 并发数
     */
    private static final int c = 50;

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
                demo4();
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJvZmZpY2VfaWQiOiIxYTcxOGU4Y2ViZmExMWU5OGFiMjdjZDMwYWViMTQ5NCIsImRldmljZVR5cGUiOiJ4Y3giLCJvZmZpY2VfbmFtZSI6IuaOqOWuoiIsInVzZXJfaWQiOiIxYTZiN2Y1NmViZmExMWU5OGFiMjdjZDMwYWViMTQ5NSIsImxvZ2luVHlwZSI6IjEiLCJ1c2VyX25hbWUiOiLotLrnpLw1NTUiLCJpc3MiOiJmaXNjYWwtdGF4IiwiZXhwIjoxNTkwMzQ5NDYxNzg3LCJpYXQiOjE1NzIzNDk0NjE3ODcsIm9mZmljZVR5cGUiOm51bGx9.N-Ngpo1zUixNEwESSJVh2oL561Z4PXp1OUoAVwIwcpU";

    /**
     * 添加客户跟进人
     */
    private void demo1() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add("1a6b7f56ebfa11e98ab27cd30aeb1494");
        jsonArray.add("1a6b7f56ebfa11e98ab27cd30aeb1494");

        String response = HttpUtils.httpPostJson("http://localhost:9186/pusher/api/v1/crm/saveFollow?id=aeb83a11f0b211e98ab27cd30aeb1494", jsonArray.toJSONString());
        log.info(response);

//        log.info(EntityUtils.toString(httpResponse.getEntity()));
    }

    /**
     * 新建线索
     */
    private void demo2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", 1);
        jsonObject.put("app_type", 1);
        jsonObject.put("name", "桑荣荣_" + ThreadLocalRandom.current().nextInt(100, 200));
        jsonObject.put("enterpriseName", "桑荣荣公司");
        jsonObject.put("position", "案源前端");
        jsonObject.put("department", "案源事业部");
        jsonObject.put("phone", "18553378628");
        jsonObject.put("phone", "telephone");
        jsonObject.put("email", "桑荣荣@icourt.cc");
        jsonObject.put("wechat", "18553378628");
        jsonObject.put("region", "北京");
        jsonObject.put("address", "北京");
        jsonObject.put("unionId", UUID.randomUUID().toString().replace("-", ""));

        String response = HttpUtils.httpPostJson("http://localhost:9186/pusher/api/v1/crm/saveClueOrCustomer", jsonObject.toJSONString(), token);
        log.info(JSON.toJSONString(response, true));

    }

    /**
     * 线索转客户
     */
    private void demo3() throws Exception {
        String sql = "select id from pusher_client_info where create_id = 'f615122af3e711e98ab27cd30aeb1494' and type = 0";

        List<Map<String, Object>> list = JdbcUtil.executeQuery(PUSHER_DEV.getConnection(), sql, null);
        for (int i = 0; i < list.size(); i += 2) {
            String id = list.get(i).get("id").toString();

            String response = HttpUtils.httpPostJson("http://localhost:9186/pusher/api/v1/crm/xcxToCustomer?id=" + id, "{}", token);
            log.info(JSON.toJSONString(response, true));
        }
    }

    /**
     * 小程序-线索列表
     */
    @Test
    public void demo4() {
        String response = HttpUtils.httpPostJson("http://localhost:9186/pusher/api/v1/crm/getXcxClueList/false?pageIndex=1&pageSize=1000", "{}", token);
        log.info(JSON.toJSONString(response, true));
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
            new Thread(() -> {
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
            }).start();
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