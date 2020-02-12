package demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.vavr.API;
import io.vavr.CheckedConsumer;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static util.HBaseKit.findOne;


/**
 * 函数式库Vavr
 * <p>
 * https://github.com/kun-song/my-blog/issues/36
 * https://blog.csdn.net/revivedsun/article/details/80088080
 *
 * @author CaoJing
 * @date 2020/02/13 02:11
 */
@Slf4j
public class VavrDemo {

    /**
     * Try.of
     */
    @Test
    public void test20200213032550() {
        Try<Integer> result = Try.of(() -> 1 / 0);
        assertTrue(result.isFailure());
    }

    /**
     * Try.of
     */
    @Test
    public void test20200213020642() {
        Try<Map<String, Object>> mapTry = Try.of(() -> findOne("judgement_ds", "35C0D1C8A8729AAC804A055C74E25055"))
            .onFailure(x -> log.error("", x));

        Map<String, Object> map = mapTry.get();

        System.out.println(JSON.toJSONString(map));
    }

    /**
     * CheckedConsumer.of().unchecked()得到不用捕获异常Consumer
     */
    @Test
    public void test20200213022839() {
        List<String> list = Lists.newArrayList("35C0D1C8A8729AAC804A055C74E25055");
        list.forEach(CheckedConsumer.of(x -> System.out.println(findOne("judgement_ds", x.toString()))).unchecked());
    }

    /**
     * API.unchecked 使用lamda不用捕获异常
     */
    @Test
    public void test20200213030956() {
        List<String> list = Lists.newArrayList("1", "l");

        // Integer.parseInt会抛出异常
        List<Integer> integerList = list.stream()
            .map(API.unchecked(x -> Integer.parseInt(x) + 8))
            .collect(Collectors.toList());

        System.out.println(integerList);
    }
}
