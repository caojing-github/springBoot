package demo;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 描述
 *
 * @author CaoJing
 * @date 2020/06/05 10:23
 */
public class StreamDemo {

    /**
     * Stream.generate：无限长度的Stream,其元素由Supplier接口的提供
     */
    @Test
    public void test20200605102404() {
        Stream<Double> stream = Stream.generate(Math::random);
        stream.forEach(System.out::println);
    }

    /**
     * Stream.iterate：返回的也是一个无限长度的Stream，与generate方法不同的是，其是通过函数f迭代对给指定的元素种子而产生无限连续有序Stream，其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环
     */
    @Test
    public void test20200605102632() {
        Stream<Integer> stream = Stream.iterate(1, item -> item + 2);
        stream.limit(10).forEach(System.out::println);
    }

    /**
     * concat 将两个Stream连接在一起，合成一个Stream。若两个输入的Stream都时排序的，则新Stream也是排序的；若输入的Stream中任何一个是并行的，则新的Stream也是并行的；若关闭新的Stream时，原两个输入的Stream都将执行关闭处理。
     */
    @Test
    public void test20200605102828() {
        Stream.concat(Stream.of(1, 2, 3), Stream.of(4, 5))
            .forEach(integer -> System.out.print(integer + "  "));
    }

    /**
     * peek 生成一个包含原Stream的所有元素的新Stream，同时会提供一个消费函数（Consumer实例)
     */
    @Test
    public void test20200605103455() {
        Stream.of("a", "b", "c")
            .peek(s -> System.out.println("peek:" + s))
            .forEach(System.out::println);
    }

    /**
     * skip 将过滤掉原Stream中的前N个元素，返回剩下的元素所组成的新Stream
     * 如果原Stream的元素个数大于N，将返回原Stream的后的元素所组成的新Stream；
     * 如果原Stream的元素个数小于或等于N，将返回一个空Stream。
     */
    @Test
    public void test20200605103545() {
        Stream.of("a", "b", "c").skip(2)
            .forEach(System.out::println);
    }

    /**
     * sorted
     */
    @Test
    public void test20200605103700() {
        Stream.of(5, 6, 3, 9, 1)
            .sorted(((o1, o2) -> o2 - o1))
            .forEach(System.out::println);

        //        Optional<Integer> max3 = Stream.of(1, 2, 3, 4, 5)
//                .max(Comparator.comparingInt(x -> x));
    }

    /**
     * forEachOrdered 与forEach类似，都是遍历Stream中的所有元素，
     * 不同的是，如果该Stream预先设定了顺序，会按照预先设定的顺序执行（Stream是无序的），默认为元素插入的顺序。
     */
    @Test
    public void test20200605103813() {
        Stream.of(5, 2, 1, 4, 3, 6, 7, 8, 9)
            .forEachOrdered(integer ->
                System.out.println("integer:" + integer)
            );
    }

    /**
     * max
     */
    @Test
    public void test20200605104309() {
        Optional<Integer> max = Stream.of(5, 2, 2, 3, 4, 8)
            .max((o1, o2) -> o2 - o1);

        int max2 = Stream.of(1, 2, 3, 4, 5)
            .mapToInt(x -> x).max().getAsInt();

        Optional<Integer> max3 = Stream.of(1, 2, 3, 4, 5)
            .max((o1, o2) -> o1 - o2);

        System.out.println("max = " + max.get() + "  max2 = " + max2 + "  max3 = " + max3.orElse(-1));
    }

    /**
     * reduce
     * reduce((sum, item) -> { ... }); //返回Optional，因为可能存在为空的情况
     * reduce(0, (sum, item) -> { ... }); /返回对应类型，不存在为空的情况
     * 无初始值，第一个参数为stream的第一个元素，第二个参数为stream的第二个元素，计算的结果赋值给下一轮计算的sum
     */
    @Test
    public void test20200605104646() {
        Optional<Integer> optional = Stream.of(1, 2, 3, 4, 5).reduce((sum, item) -> {
            System.out.println("sum before:" + sum);
            System.out.println("item:" + item);
            sum = sum + item;
            System.out.println("sum after:" + sum);

            return sum;
        });
        //等效
        Optional<Integer> optional1 = Stream.of(1, 2, 3, 4, 5).reduce((sum, item) ->
            Integer.sum(sum, item)
        );
        //等效
        Optional<Integer> optional2 = Stream.of(1, 2, 3, 4, 5).reduce(Integer::sum);
        System.out.println("integer = " + optional.orElse(-1));
    }

    /**
     * list转map法
     */
    @Test
    public void test20200605105703() {
        List<JSONObject> list = Lists.newArrayList();
        list.add(new JSONObject().fluentPut("id", "001").fluentPut("name", "caojing"));
        // 法一
        Map<String, String> map1 = list.stream().collect(Collectors.toMap(x -> x.getString("id"), x -> x.getString("name"), (v1, v2) -> v1));
        // 法二
        HashMap<Object, Object> map2 = list.stream()
            .collect(HashMap::new, (m, j) -> m.put(j.getString("id"), j.getString("name")), HashMap::putAll);
    }

    /**
     * partitioningBy：分割数据块
     */
    @Test
    public void test20200605110851() {
        Map<Boolean, List<Integer>> map1 = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.partitioningBy(item -> item > 3));
        //map1 : {false=[1, 2, 3], true=[4, 5]}
        System.out.println("map1 : " + map1);

        Map<Boolean, Long> map2 = Stream.of(1, 2, 3, 4)
            .collect(Collectors.partitioningBy(item -> item > 3, Collectors.counting()));
        System.out.println("map2: " + map2);
    }

    /**
     * 数据分组
     */
    @Test
    public void test20200605111041() {
        Map<Boolean, List<Integer>> map = Stream.of(1, 2, 3, 4, 5)
            .collect(Collectors.groupingBy(item -> item > 3));
        //map : {false=[1, 2, 3], true=[4, 5]}
        System.out.println("map : " + map);
    }
}
