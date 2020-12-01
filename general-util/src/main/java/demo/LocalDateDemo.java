package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

/**
 * LocalDate https://mp.weixin.qq.com/s/IMAGF5Nmh3amHi78B7DCMQ
 * 和SimpleDateFormat相比，DateTimeFormatter是线程安全的
 * LocalDateTime与mysql日期类型的交互（基于mybatis） https://www.cnblogs.com/carrychan/p/9883293.html
 *
 * @author CaoJing
 * @date 2019/10/23 10:24
 */
@Slf4j
public class LocalDateDemo {

    @Test
    public void test20191023102502() {

        // 毫秒数
        long x = System.currentTimeMillis() - Instant.now().toEpochMilli();
        log.info(x + "");

        // 10天前
        long y = System.currentTimeMillis() - Instant.now().minus(Duration.ofDays(10)).toEpochMilli() - 10 * 24 * 3600 * 1000;
        log.info(y + "");

        log.info(System.currentTimeMillis() / 1000 + "");
        log.info(System.currentTimeMillis() + "");

        // 获取当前年月日
        LocalDate localDate = LocalDate.now();
        //构造指定的年月日
        LocalDate localDate1 = LocalDate.of(2019, 9, 10);

        // 获取年、月、日、星期几
        int year = localDate.getYear();
        int year1 = localDate.get(ChronoField.YEAR);
        Month month = localDate.getMonth();
        int month1 = localDate.get(ChronoField.MONTH_OF_YEAR);
        int day = localDate.getDayOfMonth();
        int day1 = localDate.get(ChronoField.DAY_OF_MONTH);
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeek1 = localDate.get(ChronoField.DAY_OF_WEEK);

        // 只会获取几点几分几秒
        LocalTime localTime = LocalTime.of(13, 51, 10);
        LocalTime localTime1 = LocalTime.now();

        // 获取时分秒
        //获取小时
        int hour = localTime.getHour();
        int hour1 = localTime.get(ChronoField.HOUR_OF_DAY);
        //获取分
        int minute = localTime.getMinute();
        int minute1 = localTime.get(ChronoField.MINUTE_OF_HOUR);
        //获取秒
        int second = localTime.getSecond();
        int second1 = localTime.get(ChronoField.SECOND_OF_MINUTE);

        // 获取年月日时分秒，等于LocalDate+LocalTime
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);

        // 获取LocalDate
        LocalDate localDate2 = localDateTime.toLocalDate();
        // 获取LocalTime
        LocalTime localTime2 = localDateTime.toLocalTime();

        // 创建Instant对象
        Instant instant = Instant.now();
        // 获取秒数
        long currentSecond = instant.getEpochSecond();
        // 获取毫秒数
        long currentMilli = instant.toEpochMilli();

        // 增加、减少年数、月数、天数等
        //增加一年
        localDateTime = localDateTime.plusYears(1);
        localDateTime = localDateTime.plus(1, ChronoUnit.YEARS);
        //减少一个
        localDateTime = localDateTime.minusMonths(1);
        localDateTime = localDateTime.minus(1, ChronoUnit.MONTHS);

        // 修改某些值，修改月、日类似
        //修改年为2019
        localDateTime = localDateTime.withYear(2020);
        //修改为2022
        localDateTime = localDateTime.with(ChronoField.YEAR, 2022);

        // 返回了当前日期的第一天日期
        localDate = localDate.with(firstDayOfYear());

        // 格式化时间
        String s1 = localDate1.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = localDate1.format(DateTimeFormatter.ISO_LOCAL_DATE);
        //自定义格式化
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String s3 = localDate.format(dateTimeFormatter);

        // 解析时间
        localDate = LocalDate.parse("20190910", DateTimeFormatter.BASIC_ISO_DATE);
        localDate = LocalDate.parse("2019-09-10", DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 45天前
     */
    @Test
    public void test20200617171739() {
        // 获取当前年月日
        LocalDate localDate = LocalDate.now();
        LocalDate localDate2 = localDate.minusDays(45);
        System.out.println(localDate2);
    }

    /**
     * 最近几年
     */
    @Test
    public void test20201015192758() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        System.out.println(now.format(df));
        System.out.println(now.minusYears(1).format(df));
        System.out.println(now.minusYears(3).format(df));
        System.out.println(now.minusYears(5).format(df));
    }

    /**
     * 本月的第一天、本月的最后一天
     */
    @Test
    public void test20201016164537() {
        LocalDate today = LocalDate.now();
        // 本月的第一天
        LocalDate firstday = today.with(TemporalAdjusters.firstDayOfMonth());
        // 本月的最后一天
        LocalDate lastDay = today.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月的第一天 " + firstday);
        System.out.println("本月的最后一天 " + lastDay);
    }

    /**
     * Period、Duration
     * 相差天数
     */
    @Test
    public void test20201016173216() {
        LocalDate x = LocalDate.now();
        LocalDate y = x.minusDays(20);
        Period z = Period.between(y, x);
        // 相差天数
        System.out.println(z.getDays());

        LocalDateTime x1 = LocalDateTime.now();
        LocalDateTime y1 = x1.minusDays(15);
        Duration z1 = Duration.between(y1, x1);
        // 相差天数
        System.out.println(z1.toDays());
    }

    /**
     * 时间戳转LocalDate或LocalDateTime
     */
    @Test
    public void test20201016174628() {
        long timestamp = System.currentTimeMillis();
        // 时间戳转LocalDate
        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        // 时间戳转LocalDateTime
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        System.out.println(localDate);
        System.out.println(localDateTime);
    }

    /**
     * 今天、明天
     */
    @Test
    public void test20201021153918() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        long todayMilli = today.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        long tomorrowMilli = tomorrow.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();

        System.out.println(today + " 时间戳:" + todayMilli);
        System.out.println(tomorrow + " 时间戳:" + tomorrowMilli);
    }
}
