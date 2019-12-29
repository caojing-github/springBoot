package util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 线程池管理类
 *
 * @author CaoJing
 * @date 2019/10/31 10:07
 */
public final class ThreadPoolManager {

    private ThreadPoolManager() {
    }

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder()
        .setNameFormat("AI-pool-%d").build();

    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(1024), THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());
}
