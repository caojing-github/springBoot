package demo;

import demo.hutool.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author CaoJing
 * @date 2019/10/15 15:57
 */
@Slf4j
public class DistributedLock {

    private static Redisson redisson;
    private static final String LOCK_PREFIX = "redisLock:";

    /**
     * 初始化Redisson
     *
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    private static void initialRedisson() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://192.168.255.2:6379")
//            .setPassword(SpringContextUtils.getProperty("spring.redis.password"))
            .setIdleConnectionTimeout(50);

        redisson = (Redisson) Redisson.create(config);
    }

    /**
     * 获取Redisson
     *
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    private static synchronized Redisson getRedisson() {
        if (null == redisson) {
            initialRedisson();
        }
        return redisson;
    }

    /**
     * 获取锁
     *
     * @param lockName 锁名
     * @return boolean
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    public static boolean acquire(String lockName) {
        String key = LOCK_PREFIX + lockName;
        RLock mylock = getRedisson().getLock(key);
        mylock.lock(2, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 释放锁
     *
     * @param lockName 锁名
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    public static void release(String lockName) {
        String key = LOCK_PREFIX + lockName;
        RLock mylock = getRedisson().getLock(key);
        mylock.unlock();
    }

    public static void main(String[] args) {
        acquire("caojing666");
        release("caojing666");
    }
}
