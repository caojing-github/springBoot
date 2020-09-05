package demo;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * 线程示例
 *
 * @author CaoJing
 * @date 2020/03/23 22:24
 */
@Slf4j
@SuppressWarnings("all")
public class ThreadDemo {

    private ExecutorService SINGLE_THREAD = Executors.newSingleThreadExecutor();

    private ExecutorService FIX_THREAD = Executors.newFixedThreadPool(5);

    @Test
    public void test20200323222528() {
        AtomicInteger i = new AtomicInteger(0);
        for (int j = 0; j < 10000; j++) {
            SINGLE_THREAD.execute(() -> {
                i.incrementAndGet();
                log.info(Thread.currentThread().getId() + "开始" + i.get());
                if (i.get() == 3) {
                    SINGLE_THREAD.execute(() -> {
                        log.info(Thread.currentThread().getId() + "--> 3" + i.get());
                    });
                }
                log.info(Thread.currentThread().getId() + "结束" + i.get());
            });
        }
        while (true) ;
    }

    @Test
    public void test20200810225227() {
        List<Integer> list = Lists.newArrayList();
        Consumer<Integer> consumer = x -> {
            synchronized (this) {
                list.add(x);
            }
        };

        for (int i = 0; i < 400; i++) {
            int finalI = i;
            FIX_THREAD.execute(() -> consumer.accept(finalI));
        }
        while (true) ;
    }
}
