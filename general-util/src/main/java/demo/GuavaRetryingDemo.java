package demo;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Retryer 重试神器 https://mp.weixin.qq.com/s/ruJufCj2_bkXNTfBvA8AEA
 *
 * @author CaoJing
 * @date 2020/05/26 20:00
 */
@Slf4j
public class GuavaRetryingDemo {

    public static final Retryer<Boolean> RETRYER =
        RetryerBuilder
            .<Boolean>newBuilder()
//            .retryIfResult(Objects::isNull)
            .retryIfResult(Predicates.equalTo(false))
//            .retryIfExceptionOfType(IOException.class)
            .retryIfException()
            .retryIfRuntimeException()
            .withStopStrategy(StopStrategies.stopAfterAttempt(3))
            .withWaitStrategy(WaitStrategies.fixedWait(2, TimeUnit.SECONDS))
            .build();

    /**
     * https://www.jianshu.com/p/557eb67bb3d8
     */
    @Test
    public void test20200526200130() throws ExecutionException, RetryException {
//        Callable<Boolean> callable = () -> {
//            System.out.println(LocalDateTime.now() + "开始执行");
//            return null;
//        };
        Callable<Boolean> callable = new Callable<Boolean>() {
            int times = 1;

            @Override
            public Boolean call() throws Exception {
                log.info("call times={}", times);
                times++;

                if (times == 2) {
                    throw new NullPointerException();
                } else if (times == 3) {
                    throw new Exception();
                } else if (times == 4) {
                    throw new RuntimeException();
                } else if (times == 5) {
                    return false;
                } else {
                    return true;
                }

            }
        };
        Boolean r = RETRYER.call(callable);
        System.out.println(r);
    }
}
