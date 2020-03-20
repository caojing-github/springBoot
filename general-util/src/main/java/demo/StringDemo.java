package demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
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

    /**
     * String.join
     */
    @Test
    public void test20200320161534() {
        String join = String.join(" ", "abc", "def");
        System.out.println(join);
    }

    @Test
    public void test20200320162425() {
        String s = JSON.toJSONString(Lists.newArrayList("合同诈骗罪"));
        System.out.println(s);
    }

    @Test
    public void test20200320221941() {
        int i = 1;
        while (i-- > 0) {
            System.out.println("666");
        }
    }

    @Test
    public void test20200320223251() {
        int a = 5;
        System.out.println(5 << 1);
        System.out.println(5 << 2);
        System.out.println(5 << 3);
    }
}
