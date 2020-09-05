package demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.vavr.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
     * Option.of
     */
    @Test
    public void test20200509143945() {
        String a = null;
        System.out.println(Option.of(a).getOrElse("666"));
    }

    /**
     * 即使Optional.ofNullable中值不为null也会执行orElse中方法
     */
    @Test
    public void test20200514151119() {
        System.out.println(Optional.ofNullable("123").orElseGet(this::test20200514151120));
        System.out.println(Optional.ofNullable("123").orElse(test20200514151120()));
    }

    public String test20200514151120() {
        System.out.println("执行了");
        return "abc";
    }

    /**
     * Tuple2
     */
    @Test
    public void test20200511144257() {
        Tuple2<Integer, Integer> x = Tuple.of(0, 1);
        System.out.println(x._1);
        System.out.println(x._2);
    }

    @Test
    public void test20200217050019() {
        try {
            int x = 1 / 0;
        } finally {
            System.out.println("。。。");
        }
    }

    @Test
    public void test20200219105138() {
        Try.success(8).map(x -> null).map(y -> {
            System.out.println(y);
            return y;
        });
    }

    /**
     * Try.runRunnable
     */
    @Test
    public void test20200219100947() {
        Try.runRunnable(() -> System.out.println(1 / 0))
            .onFailure(e -> System.out.println("出错了"));
    }

    /**
     * Try.run
     */
    @Test
    public void test20200222162545() {
        Try.run(() -> System.out.println(1 / 0))
            .onFailure(e -> System.out.println("出错了"))
            .andFinally(() -> System.out.println("清理资源"));
    }

    /**
     * Try的filter不同于Optional的filter起到过滤作用，不满足条件的视为失败
     */
    @Test
    public void test20200706193041() {
        Try.success(null)
            .filter(Objects::nonNull)
            .andThenTry((CheckedConsumer<Object>) System.out::println)
            .onFailure(e -> System.out.println("执行失败了"));
    }

    /**
     * getOrElseTry
     */
    @Test
    public void test20200413134835() {
        System.out.println(Try.success(1).mapTry(x -> x / 0).getOrElseTry(() -> 2));
    }

    /**
     * Try.getOrElseThrow
     */
    @Test
    public void test20200222174814() {
        Try.run(() -> System.out.println(1 / 0))
            .getOrElseThrow((Function<Throwable, RuntimeException>) RuntimeException::new);
    }

    @Test
    public void test20200219062247() {
        Try.of(() -> 1 / 0).getOrElseThrow(e -> new RuntimeException("分母为0"));
    }

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

    /**
     * Function3
     */
    @Test
    public void test20200322192529() {
        Function3<String, String, String, String> f = (x, y, z) -> x + y + z;
        System.out.println(f.apply("曹靖", "将来", "财富自由"));
    }

    /**
     * Tuple 元组
     */
    @Test
    public void test20200707233751() {
        JSONObject jsonObject1 = new JSONObject()
            .fluentPut("name", "caojing");

        JSONObject jsonObject2 = new JSONObject()
            .fluentPut("name", "caojing666");

        JSONObject jsonObject3 = new JSONObject()
            .fluentPut("name", "caojing888");

        Tuple3<JSONObject, JSONObject, JSONObject> tuple3 = Tuple.of(jsonObject1, jsonObject2, jsonObject3);
        JSONObject t1 = tuple3._1;
        JSONObject t2 = tuple3._2;
        JSONObject t3 = tuple3._3;
    }
}
