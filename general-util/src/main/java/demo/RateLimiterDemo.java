package demo;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * 限流器
 * https://mp.weixin.qq.com/s/9SYmxU6gD2U0ATzi9gSzrQ
 * Guava 其中的RateLimiter提供了令牌桶算法实现：平滑突发限流(SmoothBursty)和平滑预热限流(SmoothWarmingUp)实现。
 *
 * @author CaoJing
 * @date 2019/11/28 20:52
 */
public class RateLimiterDemo {

    /**
     * 令牌桶算法
     */
    @Test
    public void test20191128205415() {

        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以2个的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         */
        RateLimiter r = RateLimiter.create(2);

        while (true) {
            /**
             * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
             * acquire(N)可以获取多个令牌。
             */
            System.out.println(r.acquire());
        }
    }

    /**
     * 流量平稳情况
     * 使用的RateLimiter的子类SmoothBursty
     */
    @Test
    public void test20191128205632() {

        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以210的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         * 设置缓冲时间为3秒
         */
        RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);

        while (true) {
            /**
             * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
             * acquire(N)可以获取多个令牌。
             */
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
        }
    }

    /**
     * 突发流量：突发多情况
     * 使用的RateLimiter的子类SmoothBursty
     */
    @Test
    public void test20191128205906() {

        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以210的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         * 设置缓冲时间为3秒
         */
        RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);

        while (true) {
            /**
             * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
             * acquire(N)可以获取多个令牌。
             */
            System.out.println(r.acquire(2));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
        }
    }

    /**
     * 突发流量：突发少情况
     * 使用的RateLimiter的子类SmoothBursty
     */
    @Test
    public void test20191128210011() throws InterruptedException {

        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以210的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         * 设置缓冲时间为3秒
         */
        RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);

        while (true) {
            /**
             * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
             * acquire(N)可以获取多个令牌。
             */
            System.out.println(r.acquire(1));
            Thread.sleep(2000);
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
        }
    }

    /**
     * 提供的有一定缓冲的流量输出方案
     * 使用的RateLimiter的子类SmoothWarmingUp
     */
    @Test
    public void test20191128210204() {

        /**
         * 创建一个限流器，设置每秒放置的令牌数：2个。速率是每秒可以210的消息。
         * 返回的RateLimiter对象可以保证1秒内不会给超过2个令牌，并且是固定速率的放置。达到平滑输出的效果
         * 设置缓冲时间为3秒
         */
        RateLimiter r = RateLimiter.create(2, 3, TimeUnit.SECONDS);

        while (true) {
            /**
             * acquire()获取一个令牌，并且返回这个获取这个令牌所需要的时间。如果桶里没有令牌则等待，直到有令牌。
             * acquire(N)可以获取多个令牌。
             */
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
            System.out.println(r.acquire(1));
        }
    }
}
