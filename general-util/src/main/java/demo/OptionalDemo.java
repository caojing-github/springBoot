package demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Optional;

/**
 * Optional示例
 *
 * @author CaoJing
 * @date 2019/11/05 10:11
 */
@Slf4j
public class OptionalDemo {

    @Test
    public void test20191105101144() {
        String name = "caojing";
        printName(name);

        String name2 = null;
        printName(name2);
    }

    /**
     * ifPresent()方法接受一个Consumer对象（消费函数），如果包装对象的值非空，运行Consumer对象的accept()方法
     */
    public static void printName(String name) {
        Optional.ofNullable(name).ifPresent(u -> log.info(name));
    }
}
