package util;

import java.util.Date;

/**
 * 时间处理工具
 *
 * @author CaoJing
 * @date 2019/10/16 23:03
 */
public final class DateUtil {

    /**
     * 1天对应毫秒值
     */
    private static final long DAY_LONG = 86400000L;

    /**
     * 1970-01-02 00:00:00
     */
    private static final long BEIJING_DIFF_LONG = 57600000L;

    /**
     * day=0表示今天天0点，day=1表示昨天0点，day=2表示前天0点，以此类推
     *
     * @author dujiang
     */
    public static Date dayToTime(Integer day) {
        if (null == day) {
            return null;
        }
        if (day < 0) {
            return new Date();
        }
        // 优先计算当天0点时间
        long nowLong = System.currentTimeMillis();
        long todayLong = nowLong - ((nowLong - BEIJING_DIFF_LONG) % DAY_LONG);
        return new Date(todayLong - day * DAY_LONG);
    }
}
