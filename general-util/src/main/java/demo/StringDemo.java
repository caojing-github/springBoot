package demo;

import org.junit.Test;

/**
 * 字符串操作
 *
 * @author CaoJing
 * @date 2020/03/06 16:24
 */
public class StringDemo {

    /**
     * 正数前补4个0
     */
    @Test
    public void test20200306162506() {
        String x = String.format("%04d", 123);
        System.out.println(x);
    }

    /**
     * 转2进制
     */
    @Test
    public void test20200307132400() {
        System.out.println(Integer.toBinaryString(5));
    }

    /**
     * 转16进制
     */
    @Test
    public void test20200307031723() {
        String s = "0xA";
        System.out.println(Integer.parseInt(s.substring(2), 16));
    }

    /**
     * 四舍五入
     */
    @Test
    public void test20200307042244() {
        System.out.println(Math.round(6.1));
        System.out.println(Math.round(6.5));
        System.out.println(Math.round(6.7));
    }

    /**
     * 向上取整
     */
    @Test
    public void test20200307042657() {
        System.out.println(Math.ceil(6.1));
    }

    /**
     * 向下取整
     */
    @Test
    public void test20200307042748() {
        System.out.println(Math.floor(6.1));
    }

    /**
     * 四舍五入保留小数点后1位
     */
    @Test
    public void test20200307153346() {
        System.out.println(String.format("%.1f", 0.12));
        System.out.println(String.format("%.1f", 0.15));
    }
}
