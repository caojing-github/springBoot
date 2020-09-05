package demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 字符串操作
 * org.apache.commons.lang3.StringUtils：https://www.cnblogs.com/guiblog/p/7986410.html
 *
 * @author CaoJing
 * @date 2020/03/06 16:24
 */
@Slf4j
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

    /**
     * 删除空白字符 https://www.cnblogs.com/guiblog/p/7986410.html
     */
    @Test
    public void test20200526124456() {
        String s = " a b c d ";
        System.out.println(StringUtils.deleteWhitespace(s));
    }

    @Test
    public void test20200701173934() throws IOException {
        String s = IOUtils.toString(StringDemo.class.getClassLoader().getResourceAsStream("allCaseCacheMap.json"));
        System.out.println();
    }

    /**
     * deleteWhitespace
     */
    @Test
    public void test20200713203510() {
        String s = "(一)合同定义\\n    本条第1款是关于合同定义的规定,";
        System.out.println(StringUtils.deleteWhitespace(s));
    }
}
