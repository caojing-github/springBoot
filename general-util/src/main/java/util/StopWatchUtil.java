package util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.Optional;

/**
 * 统计代码执行时长
 *
 * @author CaoJing
 * @date 2019/12/06 11:04
 */
@Slf4j
public class StopWatchUtil {

    /**
     * 计时器
     */
    private static final ThreadLocal<StopWatch> THREAD_LOCAL = ThreadLocal.withInitial(StopWatch::new);

    private StopWatchUtil() {
    }

    /**
     * 开始计时
     */
    public static void start(String taskName) {
        THREAD_LOCAL.get().start(taskName);
    }

    /**
     * 结束计时
     */
    public static void stop() {
        THREAD_LOCAL.get().stop();
    }

    /**
     * 人类可读性打印计时统计
     */
    public static void prettyPrint() {
        log.info(THREAD_LOCAL.get().prettyPrint());
    }

    /**
     * 总统计计时
     */
    public static void getTotalTimeMillis(String... prefix) {
        StopWatch sw = THREAD_LOCAL.get();
        log.info(Optional.ofNullable(prefix).map(x -> x[0]).orElse(sw.getLastTaskName()) + sw.getTotalTimeMillis());
    }

    /**
     * 清除计时器
     */
    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
